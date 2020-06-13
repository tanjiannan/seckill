--
-- Created by IntelliJ IDEA.
-- User: spike
-- Date: 2020-06-08
-- Time: 02:38
-- To change this template use File | Settings | File Templates.
--
if redis.call('set',KEYS[1],KEYS[2],'NX','PX',KEYS[3]) then
    return '1'
else
    return '0'
end

