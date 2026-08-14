import {apiClient} from './apiClient';

export type AnalyticsEventType='APP_OPEN'|'SEARCH'|'PRODUCT_VIEW'|'FAVORITE'|'ALERT_CREATED'|'NOTIFICATION_OPEN';
type Context={productId?:string;notificationId?:string};

export async function trackEvent(eventType:AnalyticsEventType,context:Context={}){
  await apiClient.post('/analytics/events',{eventType,...context});
}

export function trackEventSafely(eventType:AnalyticsEventType,context:Context={}){
  return trackEvent(eventType,context).catch(()=>undefined);
}
