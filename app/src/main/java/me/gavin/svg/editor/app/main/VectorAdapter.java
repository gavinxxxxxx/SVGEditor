package me.gavin.svg.editor.app.main;

import android.content.Context;

import java.util.List;

import me.gavin.svg.editor.R;
import me.gavin.svg.editor.app.model.Vector;
import me.gavin.svg.editor.base.recycler.RecyclerAdapter;
import me.gavin.svg.editor.base.recycler.RecyclerHolder;
import me.gavin.svg.editor.databinding.LayoutVectorBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/8/28
 */
public class VectorAdapter extends RecyclerAdapter<Vector, LayoutVectorBinding> {

    public VectorAdapter(Context context, List<Vector> list) {
        super(context, list, R.layout.layout_vector);
    }

    @Override
    protected void onBind(RecyclerHolder<LayoutVectorBinding> holder, Vector vector, int position) {
        holder.binding.vectorView.setVector(vector);
    }
}
