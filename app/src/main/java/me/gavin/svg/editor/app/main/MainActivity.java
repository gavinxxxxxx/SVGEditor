package me.gavin.svg.editor.app.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import me.gavin.svg.editor.R;
import me.gavin.svg.editor.base.BaseActivity;
import me.gavin.svg.editor.databinding.LayoutRecyclerBinding;
import me.gavin.svg.editor.util.L;
import me.gavin.svg.editor.util.VectorParser;

public class MainActivity extends BaseActivity<LayoutRecyclerBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {

        String path = "action";
        Observable.just(path)
                .map(s -> getAssets().list(s))
                .flatMap(Observable::fromArray)
                .filter(s -> s.endsWith(".svg"))
                .map(s -> getAssets().open(path + "/" + s))
                .map(VectorParser::parse)
                .map(vector -> {
                    vector.setWidth(256);
                    vector.setHeight(256);
                    return vector;
                })
                .toList()
                .subscribe(vectors -> binding.recycler.setAdapter(new VectorAdapter(this, vectors)), L::e);
    }

}
