package games.moegirl.sinocraft.sinocalligraphy.gui.not_validated_file;

import net.minecraft.util.FastColor;
import net.minecraft.util.Tuple;
import org.checkerframework.checker.units.qual.A;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;

/**
 * receive: point,time
 * return: color-map<br>
 * -------------------------------<br>
 * 通过以下思路尝试对毛笔绘制算法进行优化<br>
 * 1. 笔迹预测<br>
 * 2. 各种噪声叠加<br>
 * 3. 笔画平滑（贝塞尔）<br>
 * 4. 模糊<br>
 * -------------------------------<br>
 * Optimize brush algorithm by those function:<br>
 * 1. Handwriting prediction<br>
 * 2. Various additive noise<br>
 * 3. Smooth stroke(Bessel)<br>
 * 4. dim<br>
 */
public class BrushB implements IBrush {
    public ArrayList<brushPoint> stroke = new ArrayList<brushPoint>();
    private brushPoint brush_point;
    private ArrayList<Tuple<Tuple<Integer, Integer>, Integer>> result;
    private static int RGBCOLOR = 0x310f1b;

    public BrushB() {
        //stroke=new ArrayList<brushPoint>();
        result = new ArrayList<>();
    }

    public void onBrushDown(Tuple<Double, Double> _position, double _time) {
        stroke.add(new brushPoint(_position, _time));
    }

    public void onBrush(Tuple<Double, Double> _position, double _time) {
        stroke.add(new brushPoint(_position, _time));
        strokeSmooth();
        brushPrediction();
    }

    public ArrayList<Tuple<Tuple<Integer, Integer>, Integer>> onBrushLeft(Tuple<Double, Double> _position, double _time) {
        stroke.add(new brushPoint(_position, _time));
        strokeSmooth();
        brushPrediction();
        // noiseGenerate();
        pixelSmooth();
        return result;
    }

    public void setBrushRGB(int _colorRGB) {
        RGBCOLOR = _colorRGB;
    }

    /**
     * 处理笔迹
     *
     * @deprecated 看起来是多余的函数，它的实现将在 brushPrediction 中完成。
     */
    @Deprecated
    private void strokeSmooth() {
        // 使笔迹更平滑
    }

    /**
     * 预测笔迹方向，生成平滑笔迹\
     * <cite>
     * 计算控制点的方法是: <br>
     * 1) 设定一个0到1的系数k,在AB和BC上找到两点, b'和c', 使得距离比值, Bb' / AB = Bc' / BC = k <br>
     * 计算出两个点 b' 和 c'..(k的大小决定控制点的位置,最终决定笔迹的平滑程度, k越小, 笔迹越锐利; k越大,则笔迹越平滑.) <br>
     * 2) 然后在b' c'这条线段上再找到一个点 t, 且线段的长度满足比例: b't / tc' = AB / BC, <br>
     * 3) 把b' 和 c', 沿着 点 t 到 点B的方向移动, 直到 t 和 B重合. 由b'移动后得到 B', 由 c'移动后的距离得到B'', B'和B''就是我们要计算的位于顶点B附近的两个控制点. <br>
     * <br>
     * 实际项目过程中, 使用下面的规则进行绘制笔迹: <br>
     * 1) 当我们在手写原笔迹绘制的时候, 得到第3个点(假设分别为ABC)的时候, 可以计算出B点附近的两个控制点. <br>
     * 由于是点A为起始点,, 所以直接把点A作为第一个控制点, 计算出来的B'作为第二个控制点 <br>
     * 这样AAB'B 4个点,就可以画出点A到点B的平滑贝塞尔曲线.(或者可以直接把AB'B这3个点, 把B'作为控制点, 用二次贝塞尔曲线来拟合, 也是可以的哦~.) <br>
     * 2) 当得到第4个点(假设为D)的时候, 我们通过BCD, 计算出在点C附近的两个控制点, C'和C'', 通过BB''C'C绘制出B到C的平滑曲线.. <br>
     * 3) 当得到第i个点的时候, 进行第2个步骤......... <br>
     * 4) 当得到最后一个点Z的时候,  直接把Z作为第二个控制点(假设前一个点为Y) <br>
     * 即, 使用YY'ZZ来绘制Bezier曲线. <br>
     * 引用自：<a href="https://www.cnblogs.com/zl03jsj/p/8047259.html">88911562:原笔迹手写实现平滑和笔锋效果之:笔迹的平滑(一)</a> <br>
     * </cite>
     */
    private void brushPrediction() {
        if (stroke.size() < 3) return;
        brushPoint from = stroke.get(stroke.size() - 2);
        brushPoint to = stroke.get(stroke.size() - 1);
        Tuple<Double, Double> dir;
        // 对倒数第二个点进行操作，不对第一个点进行操作
        dir = new Tuple<Double, Double>(from.position.getA() - to.position.getA(), from.position.getB() - to.position.getB());
    }

    /**
     * 没关系，我不缺剑豪的😊我有陈sir的😊<br>
     * 艾丽妮，幸亏你不在乎我😊，不然你左右为难的话，耽误你一辈子，<br>
     * 你保重啊，再见，还会，你还会再复刻吗?<br>
     * 艾丽妮?再复刻的时候你要幸福!😊<br>
     * 好不好😊<br>
     * 艾丽妮!你要开心!你要幸福!好不好开心啊!😭幸福!😭<br>
     * 你的世界以后没有我了没关系你要幸福!!<br>
     * 在那边你好好生活好吗?<br>
     * 艾丽妮!艾丽妮!小审判官!🚕💨💨🏃🏃🏃<br>
     * 没有你我可怎么活啊😭!!<br>
     * 艾丽妮!😭😭😭艾丽妮！<br>
     * 啊啊啊啊啊啊啊😭😭😭😭😭😭😭艾丽妮，你把我带走吧，艾丽妮!😭😭😭😭<br>
     * 呜呜呜呜呜 😭😭😭😭<br>
     */
    private void noiseGenerate() {
    }

    /**
     * Rasterize
     */
    private void pixelSmooth() {
        // 暂时将每一个点都视作第一个/最后一个点,并且只处理一个像素..
        Tuple<Integer, Integer> pos = new Tuple<Integer, Integer>(-1, -1);
        Tuple<Tuple<Integer, Integer>, Integer> pixel = new Tuple<Tuple<Integer, Integer>, Integer>(pos, -1);
        for (brushPoint bp :
                stroke) {
            pos.setA(bp.position.getA().intValue());
            pos.setB(bp.position.getB().intValue());
            pixel.setA(pos);
            pixel.setB((int) (bp.time * 16));

            result.add(pixel);
        }
    }

    /**
     *
     */
    private void dstColor() {
    }
}

/**
 * 记录笔刷每一次的落点&状态
 */
class brushPoint {
    // canvas coordinate
    Tuple<Double, Double> position;
    /**
     * Time that brush stay here
     */
    double time;
    /**
     * Ink volume [0...1]
     */
    double intensity;
    /**
     * Curve slope [-1...1]
     */
    double besselLine;

    brushPoint(Tuple<Double, Double> _position, double _time) {
        position = _position;
        time = _time;
    }

    brushPoint(double x, double y, double _time) {
        position.setA(x);
        position.setB(y);
        time = _time;
    }
}