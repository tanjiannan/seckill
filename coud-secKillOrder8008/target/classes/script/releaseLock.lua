--
-- Created by IntelliJ IDEA.
-- User: spike
-- Date: 2020-06-08
-- Time: 02:39
-- To change this template use File | Settings | File Templates.
--
if redis.call('get',KEYS[1]) == KEYS[2] then
    return tostring(redis.call('del',KEYS[1]))
else
    return '0'
end

