function MetricCard({ title, value, description }) {
    return (
        <div className="metric-card">
            <h3>{title}</h3>
            <div className="value">{value}</div>

            {description && (
                <div className="metric-description">
                    {description}
                </div>
            )}
        </div>
    );
}

export default MetricCard;