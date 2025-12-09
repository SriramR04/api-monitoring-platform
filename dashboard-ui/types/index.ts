export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  email: string;
  expiresIn: number;
}

export interface ApiLog {
  id: string;
  serviceName: string;
  endpoint: string;
  method: string;
  statusCode: number;
  requestSize: number;
  responseSize: number;
  latency: number;
  timestamp: string;
  isSlowApi: boolean;
  isBrokenApi: boolean;
  isRateLimitHit: boolean;
}

export interface Alert {
  id: string;
  serviceName: string;
  endpoint: string;
  alertType: 'SLOW_API' | 'BROKEN_API' | 'RATE_LIMIT_VIOLATION';
  status: 'ACTIVE' | 'RESOLVED';
  timestamp: string;
  details: string;
  resolvedAt?: string;
  resolvedBy?: string;
  resolutionCount: number;
}

export interface DashboardStats {
  totalLogs: number;
  slowApiCount: number;
  brokenApiCount: number;
  rateLimitViolations: number;
  activeAlerts: number;
  resolvedAlerts: number;
}