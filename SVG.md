## 基本形状

代码  |  图形
----  |  ------
rect  |  矩形、圆角矩形
circle  |  圆
ellipse  |  椭圆
line  |  线段
polyline  |  折线
polygon  |  多边形
path  |  路径

## 路径

字母  |  用法  |  图形  |  说明
--  |  --  |  --  |  --
M  |  M x y <br> m dx dy  |    |  移动到
L  |  L x y <br> l dx dy  |  线段  | | 
H  |  H x <br> h dx  |  水平线段  | | 
V  |  V y <br> v dy  |  垂直线段  | | 
Q  |  Q x1 y1, x y <br> q dx1 dy1, dx dy  |  二次贝塞尔曲线  | | 
T  |  T x y <br> t dx dy  |  二次贝塞尔曲线延长线  |  控制点为前一个控制点关于前一终点的对称点  | 
C  |  C x1 y1, x2 y2, x y <br> c dx1 dy1, dx2 dy2, dx dy  |  三次贝塞尔曲线  | | 
S  |  S x2 y2, x y <br> s dx2 dy2, dx dy  |  三次贝塞尔曲线延长线   |   第一个控制点为前一个控制点关于前一终点的对称点  | 
A |  A rx ry x-axis-rotation large-arc-flag sweep-flag x y  |  弧形  |  x轴半径 、y轴半径、旋转情况、角度大小、弧线方向、弧形的终点x、弧形的终点y  | 
Z  |  Z (z)  |    |  闭合路径命令。Z命令会从当前点画一条直线到路径的起点，不区分大小写。
