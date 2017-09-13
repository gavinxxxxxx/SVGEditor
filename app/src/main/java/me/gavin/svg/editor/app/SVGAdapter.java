package me.gavin.svg.editor.app;

import android.content.Context;

import java.util.List;

import me.gavin.svg.editor.R;
import me.gavin.svg.editor.base.function.Consumer;
import me.gavin.svg.editor.base.recycler.RecyclerAdapter;
import me.gavin.svg.editor.base.recycler.RecyclerHolder;
import me.gavin.svg.editor.databinding.LayoutSvgBinding;
import me.gavin.svg.editor.svg.model.SVG;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/9/11
 */
class SVGAdapter extends RecyclerAdapter<SVG, LayoutSvgBinding> {

    private Consumer<SVG> callback;

    SVGAdapter(Context context, List<SVG> list, Consumer<SVG> callback) {
        super(context, list, R.layout.layout_svg);
        this.callback = callback;
    }

    @Override
    protected void onBind(RecyclerHolder<LayoutSvgBinding> holder, SVG svg, int position) {
        holder.binding.sv.set(svg);
        holder.binding.sv.setOnClickListener(v -> callback.accept(svg));
    }

}
