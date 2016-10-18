package no.twomonkeys.sneek.app.shared.helpers;

import android.net.Uri;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.request.ImageRequest;

/**
 * Created by simenlie on 14.10.2016.
 */

public class CacheKeyFactory extends DefaultCacheKeyFactory {

    public CacheKeyFactory() {
        super();
    }

    @Override
    public CacheKey getBitmapCacheKey(ImageRequest request) {
        SimpleCacheKey sck = new SimpleCacheKey(DataHelper.getImageCacheMapHelper().get(request.getSourceUri().toString()));
        return sck;
        //return super.getBitmapCacheKey(request);
    }

    @Override
    public CacheKey getPostprocessedBitmapCacheKey(ImageRequest request) {
        return super.getPostprocessedBitmapCacheKey(request);
    }

    @Override
    public CacheKey getEncodedCacheKey(ImageRequest request) {
        SimpleCacheKey sck = new SimpleCacheKey(DataHelper.getImageCacheMapHelper().get(request.getSourceUri().toString()));
        return sck;
    }

    @Override
    protected Uri getCacheKeySourceUri(Uri sourceUri) {
        return super.getCacheKeySourceUri(sourceUri);
    }
}