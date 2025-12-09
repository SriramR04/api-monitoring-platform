import axios from 'axios';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if available
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle auth errors
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default apiClient;

// Auth APIs
export const authAPI = {
  login: (username: string, password: string) =>
    apiClient.post('/api/auth/login', { username, password }),
};

// Logs APIs
export const logsAPI = {
  getAll: () => apiClient.get('/api/logs'),
  getByService: (serviceName: string) => apiClient.get(`/api/logs/service/${serviceName}`),
  getSlowApis: () => apiClient.get('/api/logs/slow'),
  getBrokenApis: () => apiClient.get('/api/logs/broken'),
};

// Alerts APIs
export const alertsAPI = {
  getAll: () => apiClient.get('/api/alerts'),
  getActive: () => apiClient.get('/api/alerts/active'),
  getStats: () => apiClient.get('/api/alerts/stats'),
  resolve: (alertId: string, resolvedBy: string) =>
    apiClient.put(`/api/alerts/${alertId}/resolve`, { resolvedBy }),
};