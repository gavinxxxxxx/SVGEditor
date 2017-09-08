package me.gavin.svg.editor.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.gavin.svg.editor.R;
import me.gavin.svg.editor.base.BaseActivity;
import me.gavin.svg.editor.databinding.LayoutSvgBinding;
import me.gavin.svg.editor.svg.SVGParser;
import me.gavin.svg.editor.util.L;
import me.gavin.svg.editor.util.VectorParser;

public class MainActivity extends BaseActivity<LayoutSvgBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.layout_svg;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        a();
        b();
    }

    private void a() {
//        Observable.just("action/test4.svg")
//                .map(s -> getAssets().open(s))
//                .map(VectorParser::parse)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(L::e, L::e);
    }

    private void b() {
        Observable.just("action/test3.svg")
                .map(s -> getAssets().open(s))
                .map(SVGParser::parse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> binding.sv.set(v), L::e);
    }

}
