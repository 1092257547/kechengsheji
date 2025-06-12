import axios from 'axios';

const http = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 10000
});

// 请求拦截器
http.interceptors.request.use(
    config => {
        // 添加token到请求头
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        console.error('请求错误:', error);
        return Promise.reject(error);
    }
);

// 响应拦截器
http.interceptors.response.use(
    response => {
        return response.data;
    },
    error => {
        console.error('响应错误:', error);
        if (error.response && error.response.status === 401) {
            // token过期或无效，跳转到登录页
            localStorage.removeItem('user');
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default http;
