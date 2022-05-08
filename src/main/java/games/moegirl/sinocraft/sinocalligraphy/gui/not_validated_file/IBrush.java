package games.moegirl.sinocraft.sinocalligraphy.gui.not_validated_file;

import net.minecraft.util.Tuple;

import java.util.ArrayList;

public interface IBrush {
    public void onBrushDown(Tuple<Double,Double> _position, double _time);
    public void onBrush(Tuple<Double,Double> _position,double _time);
    public ArrayList<Tuple<Tuple<Integer, Integer>,Integer>> onBrushLeft(Tuple<Double,Double> _position, double _time);
    public void setBrushRGB(int _colorRGB);
}
