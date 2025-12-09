'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { logsAPI, alertsAPI } from '@/lib/api/client';
import { ApiLog, Alert, DashboardStats } from '@/types';
import StatsCard from '@/components/StatsCard';
import LogsTable from '@/components/LogsTable';
import AlertsPanel from '@/components/AlertsPanel';
import { Activity, AlertCircle, Clock, TrendingUp } from 'lucide-react';

export default function DashboardPage() {
  const router = useRouter();
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [logs, setLogs] = useState<ApiLog[]>([]);
  const [alerts, setAlerts] = useState<Alert[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      router.push('/login');
      return;
    }

    fetchData();
    // Refresh data every 10 seconds
    const interval = setInterval(fetchData, 10000);
    return () => clearInterval(interval);
  }, [router]);

  const fetchData = async () => {
    try {
      const [statsRes, logsRes, alertsRes] = await Promise.all([
        alertsAPI.getStats(),
        logsAPI.getAll(),
        alertsAPI.getActive(),
      ]);

      setStats(statsRes.data);
      setLogs(logsRes.data);
      setAlerts(alertsRes.data);
    } catch (error) {
      console.error('Failed to fetch data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    router.push('/login');
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-xl">Loading dashboard...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-gray-900">
            API Monitoring Dashboard
          </h1>
          <button
            onClick={handleLogout}
            className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
          >
            Logout
          </button>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <StatsCard
            title="Total API Calls"
            value={stats?.totalLogs || 0}
            icon={Activity}
            color="blue"
          />
          <StatsCard
            title="Slow APIs"
            value={stats?.slowApiCount || 0}
            icon={Clock}
            color="yellow"
          />
          <StatsCard
            title="Broken APIs"
            value={stats?.brokenApiCount || 0}
            icon={AlertCircle}
            color="red"
          />
          <StatsCard
            title="Active Alerts"
            value={stats?.activeAlerts || 0}
            icon={TrendingUp}
            color="purple"
          />
        </div>

        {/* Alerts Panel */}
        {alerts.length > 0 && (
          <div className="mb-8">
            <AlertsPanel alerts={alerts} onRefresh={fetchData} />
          </div>
        )}

        {/* Logs Table */}
        <div className="bg-white rounded-lg shadow-md">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-800">Recent API Logs</h2>
          </div>
          <LogsTable logs={logs} />
        </div>
      </main>
    </div>
  );
}