import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 50 },
        { duration: '30s', target: 50 },
        { duration: '10s', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(99)<300'],
    },
};

export default function () {
    const companyId = 1;
    const date = encodeURIComponent('2026/06/30');
    const service = encodeURIComponent('Ultra Haircut');
    const duration = 30;

    const url = `http://localhost:8081/hours/list?companyId=${companyId}&date=${date}&service=${service}&duration=${duration}`;
    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.get(url, params);

    check(res, {
        'status is 200': (r) => r.status === 200,
        'returns valid JSON array': (r) => r.body.length > 0 && r.body.includes('dateTime'),
    });

    sleep(1);
}