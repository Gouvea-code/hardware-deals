const root = document.querySelector('#live-offers');
const apiUrl = (window.HARDWARE_DEALS_API_URL || '').replace(/\/$/, '');
const money = value => new Intl.NumberFormat('pt-BR', {style: 'currency', currency: 'BRL'}).format(value);
const escapeHtml = value => String(value).replace(/[&<>"']/g, character => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[character]));
const safeUrl = value => { try { const url = new URL(value); return url.protocol === 'https:' ? escapeHtml(url.href) : '#'; } catch { return '#'; } };
if (!apiUrl) {
  root.innerHTML = '<p class="notice">A lista pública será exibida assim que a API de produção for conectada.</p>';
} else {
  fetch(`${apiUrl}/api/v1/deals?sort=score`).then(response => {
    if (!response.ok) throw new Error('API indisponível'); return response.json();
  }).then(deals => {
    root.innerHTML = deals.length ? deals.slice(0, 12).map(deal => `<article class="card offer"><div class="store">${escapeHtml(deal.storeName)}</div><h3>${escapeHtml(deal.productName)}</h3><strong>${money(deal.price)}</strong><span>Deal Score ${Number(deal.score)}</span><a class="cta" href="${safeUrl(deal.url)}" rel="noopener noreferrer">Ver oferta</a></article>`).join('') : '<p class="notice">Nenhuma oferta disponível neste momento.</p>';
  }).catch(() => { root.innerHTML = '<p class="notice">Não foi possível carregar os preços. Tente novamente em instantes.</p>'; });
}
