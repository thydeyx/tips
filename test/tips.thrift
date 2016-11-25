namespace java com.meituan.dataapp.service.tips.thrift

// 公共部分
struct TipsStidInfo
{
    1: required string globalId = ""; // 请求id
    2: required string strategy = ""; // 策略名
    3: optional string alg,
    4: optional string server,
    5: optional map<string, string> kv,
    6: optional map<string, string> ext,
}

// wifi信息
struct TWifi
{
    1: string MACaddress, // WiFi MAC address
    2: i32 strength, // 信号强度，负数值
    3: string name, // WiFi 名字
    4: bool isConnected = false, // 当前是否连接
}

// 位置信息
struct Location
{
    1: required double latitude = -200; // 超出正常范围，表示没有设置纬度
    2: required double longitude = -200; // 超出正常范围，表示没有设置经度
    3: optional list<TWifi> wifis; // 用户当前连接或周边的WiFi
    40: optional map<string, string> exdata; // 扩展字段
}

// 筛选信息
struct SelectMsg
{
    1: optional string areaid = ""; // 地域信息
    2: optional string cateid = ""; // 品类信息
    3: optional string sort = ""; // 排序
    40: optional map<string, string> exdata; // 扩展字段
}

/*
enum ReqType
{
    REQ_TYPE_NON = 0,
    REQ_TYPE_POI_LIST = 1,
}
*/

// 推荐Query信息
struct TipsItem
{
    1: required string word; // 推荐词
    2: optional i32 count; // 推荐词结果数
    3: optional string stg; // 召回排序策略
    4: optional double score; // 排序分数
    5: optional string type; // 出词类型
    40: optional map<string, string> exdata;
}

// 请求上下文信息
struct TipsReq
{
    1: required i32 reqid = 1; // 业务请求id
    2: required i32 cityid; // 输入城市id
    3: required string query; // 输入query
    4: required i32 userid = -1; // 用户id
    5: required string uuid = ""; // 用户uuid
    6: required string device = ""; // 设备信息
    7: required i32 limit = 15; // 请求结果数
    8: required i32 length = -1; // 出词长度限制, -1代表不限制
    9: required string strategy = ""; // debug, norerank等请求策略名
    10: required string traceId = ""; // 前端生成跟踪字段
    11: optional Location location; // 位置信息
    12: optional SelectMsg selectMsg; // 高级筛选项
    40: optional map<string, string> exdata; // 扩展字段
}

struct TipsRes
{
    1: required list<TipsItem> tipsList; // 推荐词信息
    2: required TipsStidInfo stidInfo;
    40: optional map<string, string> exdata;
}

service SearchTips
{
    TipsRes GetTips(1: TipsReq req);
}
