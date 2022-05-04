package games.moegirl.sinocraft.sinocalligraphy.gui.not_validated_file;

import net.minecraft.util.Tuple;
import org.checkerframework.checker.units.qual.A;

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
    public ArrayList<brushPoint> stroke;
    private brushPoint brush_point;
    private ArrayList<Tuple<Tuple<Integer, Integer>,Integer>> result;
    private static int RGBCOLOR=0x310f1b;

    public void Brush_planB(){
      stroke=new ArrayList<brushPoint>();
      result=new ArrayList<Tuple<Tuple<Integer, Integer>,Integer>>();
    }

    public void onBrushDown(Tuple<Double,Double> _position,double _time){
        stroke.add(new brushPoint(_position,_time));
        strokeSmooth();
    }
    public void onBrush(Tuple<Double,Double> _position,double _time){
        stroke.add(new brushPoint(_position,_time));
        strokeSmooth();
        brushPrediction();
    }
    public ArrayList<Tuple<Tuple<Integer, Integer>,Integer>> onBrushLeft(Tuple<Double,Double> _position,double _time){
        stroke.add(new brushPoint(_position,_time));
        strokeSmooth();
        brushPrediction();
        noiseGenerate();
        pixelSmooth();
        return result;
    }

    public void setColorRGB(int _colorRGB){
        RGBCOLOR=_colorRGB;
    }

    /** 处理笔迹 */
    private void strokeSmooth(){
        // 使笔迹更平滑
    }
    private void brushPrediction(){
        // 预测笔迹方向
    }
    private void noiseGenerate(){}
    private void pixelSmooth(){}
}

/** 记录笔刷每一次的落点&状态 */
class brushPoint{
    // canvas coordinate
    Tuple<Double, Double> position;
    Tuple<Double, Double> direction=null;
    double time;
    // how many ink the paper received.
    // *not ink out*
    double intensity;
    // how smooth the stroke is
    double bessel_index=0.5f;
    
    brushPoint(Tuple<Double, Double> _position,double _time){
        position =_position;
        time=_time;
    }
    brushPoint(double x,double y,double _time){
        position.setA(x);
        position.setB(y);
        time= _time;
    }
}