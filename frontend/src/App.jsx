import './App.css'
import MetricCard from "./components/MetricCard.jsx";
import CachePerformance from "./components/CachePerformance.jsx";
import { useEffect, useState } from "react";

function App() {

  const [metrics, setMetrics] = useState({
    totalRequests: 0,
    allowedRequests: 0,
    rejectedRequests: 0,
    cacheHits: 0,
    cacheMisses: 0
  });

  useEffect(() => {

    const fetchMetrics = () => {
      fetch("http://localhost:8080/metrics")
          .then(response => response.json())
          .then(data => {
            setMetrics(data);
          })
          .catch(error => {
            console.error("Failed to fetch metrics:", error);
          });
    };

    // Fetch immediately when dashboard loads
    fetchMetrics();

    // Fetch every 2 seconds
    const interval = setInterval(fetchMetrics, 2000);

    // Stop the interval when component is removed
    return () => clearInterval(interval);

  }, []);

  return (
      <div className="dashboard">

        <header className="header">
          <div>
            <h1>RateFlow</h1>
            <p>Distributed Rate Limiter</p>
          </div>

          <div className="status">
            <span></span>
            SYSTEM ONLINE
          </div>
        </header>

        <main>

          <section className="metrics">

            <MetricCard
                title="Total Requests"
                value={metrics.totalRequests}
                description="Requests received"
            />

            <MetricCard
                title="Allowed"
                value={metrics.allowedRequests}
                description="Requests accepted"
            />

            <MetricCard
                title="Rejected"
                value={metrics.rejectedRequests}
                description="Rate limited"
            />

          </section>

          <section className="content-grid">

            <div className="panel traffic">
              <h2>Request Traffic</h2>
            </div>

            <div className="panel cache">
              <h2>Cache Performance</h2>

              <CachePerformance
                  hits={metrics.cacheHits}
                  misses={metrics.cacheMisses}
              />

            </div>

          </section>

          <section className="panel rate-limit">
            <h2>Rate Limit Distribution</h2>
          </section>

        </main>

      </div>
  );
}

export default App;