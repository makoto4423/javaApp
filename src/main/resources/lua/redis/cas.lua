local v = redis.call("get", KEYS[1])
local r = 1
if v == KEYS[2] or v == false then
    redis.call("set", KEYS[1], KEYS[3] ,"ex", "30")
else
    r = 0
end
return {r, v}