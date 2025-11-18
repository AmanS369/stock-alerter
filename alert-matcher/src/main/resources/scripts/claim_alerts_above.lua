-- KEYS[1] = key for active alerts (e.g. alerts:AAPL:ABOVE)
-- KEYS[2] = key for processing alerts (e.g. alerts:AAPL:ABOVE:processing)
-- ARGV[1] = current price

-- Fetch alerts whose target price <= current price (triggered)
local alerts = redis.call('ZRANGEBYSCORE', KEYS[1], '-inf', ARGV[1])

for _, v in ipairs(alerts) do
  redis.call('ZREM', KEYS[1], v)
  redis.call('ZADD', KEYS[2], ARGV[1], v)
end

return alerts
