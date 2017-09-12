package me.gavin.svg.editor.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

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
        Observable.just("", "action")
                .flatMap(path -> Observable.just(path)
                        .map(getAssets()::list)
                        .flatMap(Observable::fromArray)
                        .filter(s -> s.endsWith(".svg"))
                        .map(s -> String.format("%s%s",
                                TextUtils.isEmpty(path) ? "" : path + "/", s)))
                .map(getAssets()::open)
                .map(SVGParser::parse)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(vectors ->
                        binding.recycler.setAdapter(new SVGAdapter(this, vectors, svg -> {
                            binding.sv.set(svg);
                            binding.sv.setZoomable(true);
                        })), L::e);
    }

}
