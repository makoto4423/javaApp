local v = redis.call("get", KEYS[1])
if tonumber(v) >= tonumber(KEYS[2]) then
    redis.call("set", KEYS[1], v - KEYS[2])
    return true
else
    return false
end