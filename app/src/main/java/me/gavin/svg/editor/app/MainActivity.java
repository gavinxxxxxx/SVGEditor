package me.gavin.svg.editor.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.gavin.svg.editor.R;
import me.gavin.svg.editor.base.BaseActivity;
import me.gavin.svg.editor.databinding.LayoutRecyclerBinding;
import me.gavin.svg.editor.svg.parser.SVGParser;
import me.gavin.svg.editor.util.L;

public class MainActivity extends BaseActivity<LayoutRecyclerBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        a();
        s();
    }

    private void a() {
//        Observable.just("action/ic_accessible_24px.svg")
//                .map(s -> getAssets().open(s))
//                .map(SVGParser::parse)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(v -> binding.sv.set(v), L::e);
    }

    private void s() {
        String path = "action";
        Observable.just(path)
                .map(s -> getAssets().list(s))
                .flatMap(Observable::fromArray)
                .filter(s -> s.endsWith(".svg"))
                .map(s -> getAssets().open(path + "/" + s))
                .map(SVGParser::parse)
                .map(svg -> {
                    svg.setWidth(256);
                    svg.setHeight(256);
                    L.e(svg);
                    return svg;
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(vectors -> {
                    binding.recycler.setAdapter(new SVGAdapter(this, vectors));
                }, L::e);
    }

}
