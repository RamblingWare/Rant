package org.oddox.action.interceptor;

import java.util.List;

import org.oddox.config.Application;
import org.oddox.objects.Category;
import org.oddox.objects.Post;
import org.oddox.objects.Tag;
import org.oddox.objects.Year;

import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * Archive Interceptor class
 * 
 * @author Austin Delamar
 * @date 11/30/2015
 */
public class ArchiveInterceptor implements Handler<RoutingContext> {

    public static final long EXPIRETIME = 86400000l;
    public static long cacheTime = 0l;
    public static int archiveTotal = 0;

    private static List<Post> archiveFeatured = null;
    private static List<Year> archiveYears = null;
    private static List<Tag> archiveTags = null;
    private static List<Category> archiveCategories = null;

    @Override
    public void handle(RoutingContext context) {

        // Has it been 24 hours since fresh archive check?
        long diff = Math.abs(System.currentTimeMillis() - cacheTime);
        if (diff >= EXPIRETIME) {
            // cache expired.
            // get fresh archive metadata

            // get the archive of posts by years and tag names
            archiveFeatured = Application.getDatabaseService()
                    .getFeatured();
            archiveYears = Application.getDatabaseService()
                    .getYears();
            archiveTags = Application.getDatabaseService()
                    .getTags();
            archiveCategories = Application.getDatabaseService()
                    .getCategories();

            // count total posts
            archiveTotal = 0;
            for (Year year : archiveYears) {
                archiveTotal += year.getCount();
            }

            // set new cacheTime
            cacheTime = System.currentTimeMillis();
        }
        // else,
        // just use cache archive metadata,
        // which at this point is already set.

        // set to context
        context.put("archiveTotal", archiveTotal);
        context.put("archiveFeatured", archiveFeatured);
        context.put("archiveYears", archiveYears);
        context.put("archiveTags", archiveTags);
        context.put("archiveCategories", archiveCategories);

        context.next();
    }

    public static List<Post> getArchiveFeatured() {
        return archiveFeatured;
    }

    public static void setArchiveFeatured(List<Post> archiveFeatured) {
        ArchiveInterceptor.archiveFeatured = archiveFeatured;
    }

    public static List<Year> getArchiveYears() {
        return archiveYears;
    }

    public static void setArchiveYears(List<Year> archiveYears) {
        ArchiveInterceptor.archiveYears = archiveYears;
    }

    public static List<Tag> getArchiveTags() {
        return archiveTags;
    }

    public static void setArchiveTags(List<Tag> archiveTags) {
        ArchiveInterceptor.archiveTags = archiveTags;
    }

    public static List<Category> getArchiveCategories() {
        return archiveCategories;
    }

    public static void setArchiveCategories(List<Category> archiveCategories) {
        ArchiveInterceptor.archiveCategories = archiveCategories;
    }
}
