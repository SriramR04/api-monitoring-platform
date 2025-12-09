import { useState } from 'react';
import { Alert } from '@/types';
import { alertsAPI } from '@/lib/api/client';
import { AlertCircle, Clock, AlertTriangle, CheckCircle } from 'lucide-react';
import { formatDistanceToNow } from 'date-fns';

interface AlertsPanelProps {
  alerts: Alert[];
  onRefresh: () => void;
}

export default function AlertsPanel({ alerts, onRefresh }: AlertsPanelProps) {
  const [resolving, setResolving] = useState<string | null>(null);

  const handleResolve = async (alertId: string) => {
    setResolving(alertId);
    try {
      await alertsAPI.resolve(alertId, 'admin');
      onRefresh();
    } catch (error) {
      console.error('Failed to resolve alert:', error);
    } finally {
      setResolving(null);
    }
  };

  const getAlertIcon = (type: string) => {
    switch (type) {
      case 'SLOW_API':
        return <Clock className="text-yellow-600" size={20} />;
      case 'BROKEN_API':
        return <AlertCircle className="text-red-600" size={20} />;
      case 'RATE_LIMIT_VIOLATION':
        return <AlertTriangle className="text-orange-600" size={20} />;
      default:
        return <AlertCircle size={20} />;
    }
  };

  const getAlertColor = (type: string) => {
    switch (type) {
      case 'SLOW_API':
        return 'border-yellow-300 bg-yellow-50';
      case 'BROKEN_API':
        return 'border-red-300 bg-red-50';
      case 'RATE_LIMIT_VIOLATION':
        return 'border-orange-300 bg-orange-50';
      default:
        return 'border-gray-300 bg-gray-50';
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <h2 className="text-xl font-semibold text-gray-800 mb-4 flex items-center gap-2">
        <AlertCircle className="text-red-600" size={24} />
        Active Alerts ({alerts.length})
      </h2>

      <div className="space-y-4">
        {alerts.map((alert) => (
          <div
            key={alert.id}
            className={`border-l-4 rounded-lg p-4 ${getAlertColor(alert.alertType)}`}
          >
            <div className="flex items-start justify-between">
              <div className="flex items-start gap-3 flex-1">
                {getAlertIcon(alert.alertType)}
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-1">
                    <span className="font-semibold text-gray-900">
                      {alert.serviceName}
                    </span>
                    <span className="text-gray-600">•</span>
                    <span className="text-gray-700">{alert.endpoint}</span>
                  </div>
                  <p className="text-sm text-gray-600 mb-2">{alert.details}</p>
                  <div className="flex items-center gap-4 text-xs text-gray-500">
                    <span>{formatDistanceToNow(new Date(alert.timestamp), { addSuffix: true })}</span>
                    {alert.resolutionCount > 0 && (
                      <span className="flex items-center gap-1">
                        <CheckCircle size={12} />
                        Resolved {alert.resolutionCount} time(s)
                      </span>
                    )}
                  </div>
                </div>
              </div>

              {alert.status === 'ACTIVE' && (
                <button
                  onClick={() => handleResolve(alert.id)}
                  disabled={resolving === alert.id}
                  className="ml-4 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors text-sm font-medium disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {resolving === alert.id ? 'Resolving...' : 'Resolve'}
                </button>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}