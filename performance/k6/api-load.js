import http from 'k6/http';
import { check, sleep } from 'k6';

const profile = __ENV.LOAD_PROFILE || '100';
const targets = {
  '100': [{ duration: '30s', target: 100 }, { duration: '30s', target: 0 }],
  '1000': [{ duration: '2m', target: 1000 }, { duration: '1m', target: 0 }],
  '10000': [{ duration: '5m', target: 10000 }, { duration: '2m', target: 0 }],
};

export const options = {
  stages: targets[profile] || targets['100'],
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<500', 'p(99)<1000'],
  },
};

const baseUrl = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  const responses = http.batch([
    ['GET', `${baseUrl}/api/v1/health`],
    ['GET', `${baseUrl}/api/v1/products?page=0&size=20`],
    ['GET', `${baseUrl}/api/v1/deals?page=0&size=20`],
  ]);
  responses.forEach(response => check(response, { 'status is 200': value => value.status === 200 }));
  sleep(1);
}
