local tokens = redis.call("HGET", KEYS[1], "tokens")
local lastRefill = redis.call("HGET", KEYS[1], "lastRefill")

local capacity = tonumber(ARGV[1])
local refillRate = tonumber(ARGV[2])
local currentTime = tonumber(ARGV[3])

if tokens == false then
	tokens = capacity
	lastRefill = currentTime
else
	tokens = tonumber(tokens)
	lastRefill = tonumber(lastRefill)
end

local elapsed = currentTime - lastRefill
local refill = elapsed * refillRate

tokens = math.min(capacity, tokens + refill)

local allowed = 0

if tokens >= 1 then
	tokens = tokens - 1
	allowed = 1
end

redis.call("HSET", KEYS[1],
	"tokens", tokens,
	"lastRefill", currentTime)

return allowed