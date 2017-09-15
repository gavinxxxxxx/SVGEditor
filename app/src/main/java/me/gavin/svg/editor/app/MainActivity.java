package me.gavin.svg.editor.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

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
        Observable.just("", "size", "action")
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
                        binding.recycler.setAdapter(new SVGAdapter(this, vectors, svg ->
                                binding.sv.set(svg))), L::e);

        Observable.just(getIntent())
                .filter(i -> i.getAction().equals(Intent.ACTION_VIEW))
                .map(Intent::getData)
                .map(getContentResolver()::openInputStream)
                .map(SVGParser::parse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(binding.sv::set, L::e);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.e, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        performFileSearch();
        return true;
    }

    private static final int READ_REQUEST_CODE = 42;

    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/svg+xml");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Observable.just(0)
                .filter(a -> requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK)
                .filter(a -> data != null && data.getData() != null)
                .map(a -> data.getData())
                .map(getContentResolver()::openInputStream)
                .map(SVGParser::parse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(binding.sv::set, L::e);
    }

}
