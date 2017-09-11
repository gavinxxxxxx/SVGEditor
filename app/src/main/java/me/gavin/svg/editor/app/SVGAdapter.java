package me.gavin.svg.editor.app;

import android.content.Context;

import java.util.List;

import me.gavin.svg.editor.R;
import me.gavin.svg.editor.base.recycler.RecyclerAdapter;
import me.gavin.svg.editor.base.recycler.RecyclerHolder;
import me.gavin.svg.editor.databinding.LayoutSvgBinding;
import me.gavin.svg.editor.svg.model.SVG;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/9/11
 */
public class SVGAdapter extends RecyclerAdapter<SVG, LayoutSvgBinding> {

    public SVGAdapter(Context context, List<SVG> list) {
        super(context, list, R.layout.layout_svg);
    }

    @Override
    protected void onBind(RecyclerHolder<LayoutSvgBinding> holder, SVG svg, int position) {
        holder.binding.sv.set(svg);
    }
}
