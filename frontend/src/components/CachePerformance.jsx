function CachePerformance({ hits, misses }) {

  const total = hits + misses;

  const hitRate = total === 0
    ? 0
    : ((hits / total) * 100).toFixed(1);

  return (
    <div className="cache-content">

      <div className="cache-rate">
        <div className="cache-rate-value">
          {hitRate}%
        </div>

        <div className="cache-rate-label">
          HIT RATE
        </div>
      </div>

      <div className="cache-bar">
        <div
          className="cache-bar-fill"
          style={{ width: `${hitRate}%` }}
        />
      </div>

      <div className="cache-stats">

        <div>
          <span className="stat-label">Hits</span>
          <strong>{hits}</strong>
        </div>

        <div>
          <span className="stat-label">Misses</span>
          <strong>{misses}</strong>
        </div>

      </div>

    </div>
  );
}

export default CachePerformance;