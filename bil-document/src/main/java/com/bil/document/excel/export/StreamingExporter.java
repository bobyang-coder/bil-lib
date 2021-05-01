package com.bil.document.excel.export;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by fan.tang on 2017/6/16.
 */
public interface StreamingExporter {

    <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs) throws IOException;

    <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs, boolean lockResultSheet) throws IOException;

    <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs, String title) throws IOException;

    <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs, String title, int sheetRowMaxSize) throws IOException;

}
