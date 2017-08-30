package me.gavin.svg.editor.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.gavin.svg.editor.R;
import me.gavin.svg.editor.base.BaseActivity;
import me.gavin.svg.editor.databinding.LayoutVectorBinding;
import me.gavin.svg.editor.util.L;
import me.gavin.svg.editor.util.VectorParser;

public class MainActivity extends BaseActivity<LayoutVectorBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.layout_vector;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        a();
        list();
    }

    private void a() {
        Observable.just("action/test3.svg")
                .map(s -> getAssets().open(s))
                .map(VectorParser::parse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> binding.vectorView.setVector(v), L::e);
    }

    private void list() {
//        String path = "action";
//        Observable.just(path)
//                .map(s -> getAssets().list(s))
//                .flatMap(Observable::fromArray)
//                .filter(s -> s.endsWith(".svg"))
//                .map(s -> getAssets().open(path + "/" + s))
//                .map(VectorParser::parse)
//                .map(vector -> {
//                    vector.setWidth(256);
//                    vector.setHeight(256);
//                    return vector;
//                })
//                .toList()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(vectors -> {
//                    binding.recycler.setAdapter(new VectorAdapter(this, vectors));
//                }, L::e);
    }

}
