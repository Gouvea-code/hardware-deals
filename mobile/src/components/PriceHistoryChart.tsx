import {useMemo, useState} from 'react';
import {Pressable, StyleSheet, View} from 'react-native';
import Svg, {Circle, Line, Polyline} from 'react-native-svg';

import {colors, spacing, typography} from '../theme';
import {PricePoint} from '../types/api';
import {formatCurrency} from '../utils/formatCurrency';
import {AppText} from './AppText';

type Period = 7 | 30 | 'all';
const WIDTH = 320;
const HEIGHT = 150;
const PADDING = 16;

export function PriceHistoryChart({history}: {history: PricePoint[]}) {
  const [period, setPeriod] = useState<Period>(30);
  const filtered = useMemo(() => filterByPeriod(history, period), [history, period]);
  const chart = useMemo(() => buildChart(filtered), [filtered]);

  if (!history.length) {
    return <AppText style={styles.empty}>Sem dados para exibir no gráfico.</AppText>;
  }

  return (
    <View>
      <View accessibilityRole="tablist" style={styles.periods}>
        {([7, 30, 'all'] as Period[]).map(value => (
          <Pressable
            accessibilityRole="tab"
            accessibilityState={{selected: period === value}}
            key={value}
            onPress={() => setPeriod(value)}
            style={[styles.period, period === value && styles.periodSelected]}>
            <AppText style={[styles.periodText, period === value && styles.periodTextSelected]}>
              {value === 'all' ? 'Tudo' : `${value} dias`}
            </AppText>
          </Pressable>
        ))}
      </View>

      {!chart ? (
        <AppText style={styles.empty}>Sem dados neste período.</AppText>
      ) : (
        <>
          <View style={styles.metrics}>
            <Metric label="Mínimo" value={formatCurrency(chart.minimum)} />
            <Metric label="Máximo" value={formatCurrency(chart.maximum)} />
            <Metric label="Atual" value={formatCurrency(chart.current)} />
          </View>
          <View accessibilityLabel="Gráfico do histórico de preços" style={styles.chart}>
            <Svg height="100%" viewBox={`0 0 ${WIDTH} ${HEIGHT}`} width="100%">
              {[0, 0.5, 1].map(position => {
                const y = PADDING + position * (HEIGHT - PADDING * 2);
                return <Line key={position} stroke={colors.border} strokeWidth="1" x1={PADDING} x2={WIDTH - PADDING} y1={y} y2={y} />;
              })}
              <Polyline fill="none" points={chart.points} stroke={colors.primary} strokeLinejoin="round" strokeWidth="3" />
              <Circle cx={chart.last.x} cy={chart.last.y} fill={colors.primary} r="5" />
            </Svg>
          </View>
        </>
      )}
    </View>
  );
}

function Metric({label, value}: {label: string; value: string}) {
  return (
    <View style={styles.metric}>
      <AppText style={styles.metricLabel}>{label}</AppText>
      <AppText style={styles.metricValue}>{value}</AppText>
    </View>
  );
}

export function filterByPeriod(history: PricePoint[], period: Period) {
  if (period === 'all' || !history.length) return history;
  const newest = Math.max(...history.map(point => new Date(point.collectedAt).getTime()));
  const cutoff = newest - period * 24 * 60 * 60 * 1000;
  return history.filter(point => new Date(point.collectedAt).getTime() >= cutoff);
}

export function buildChart(history: PricePoint[]) {
  if (!history.length) return null;
  const prices = history.map(point => point.price);
  const minimum = Math.min(...prices);
  const maximum = Math.max(...prices);
  const range = maximum - minimum || 1;
  const drawableWidth = WIDTH - PADDING * 2;
  const drawableHeight = HEIGHT - PADDING * 2;
  const coordinates = history.map((point, index) => ({
    x: PADDING + (history.length === 1 ? drawableWidth / 2 : (index / (history.length - 1)) * drawableWidth),
    y: PADDING + ((maximum - point.price) / range) * drawableHeight,
  }));

  return {
    current: history.at(-1)!.price,
    last: coordinates.at(-1)!,
    maximum,
    minimum,
    points: coordinates.map(point => `${point.x},${point.y}`).join(' '),
  };
}

const styles = StyleSheet.create({
  chart: {height: HEIGHT, marginTop: spacing.md},
  empty: {color: colors.textMuted, paddingVertical: spacing.lg, textAlign: 'center'},
  metric: {flex: 1},
  metricLabel: {color: colors.textMuted, fontSize: 12},
  metrics: {flexDirection: 'row', gap: spacing.sm, marginTop: spacing.md},
  metricValue: {fontSize: typography.sizes.label, fontWeight: typography.weights.bold, marginTop: spacing.xs},
  period: {borderRadius: 999, paddingHorizontal: spacing.md, paddingVertical: spacing.sm},
  periods: {backgroundColor: colors.background, borderRadius: 999, flexDirection: 'row', padding: spacing.xs},
  periodSelected: {backgroundColor: colors.primary},
  periodText: {color: colors.textMuted, fontSize: typography.sizes.label},
  periodTextSelected: {color: colors.onPrimary, fontWeight: typography.weights.bold},
});
