import io.xpipe.core.process.ProcessControlProvider;
import io.xpipe.core.process.ShellDialect;
import io.xpipe.core.process.ShellDialects;
import io.xpipe.core.util.CoreJacksonModule;
import io.xpipe.core.util.ModuleLayerLoader;

open module io.xpipe.core {
    exports io.xpipe.core.store;
    exports io.xpipe.core.util;
    exports io.xpipe.core.dialog;
    exports io.xpipe.core.charsetter;
    exports io.xpipe.core.process;

    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.module.paramnames;
    requires static com.fasterxml.jackson.core;
    requires static com.fasterxml.jackson.databind;
    requires java.net.http;
    requires static lombok;

    uses com.fasterxml.jackson.databind.Module;
    uses ProcessControlProvider;
    uses io.xpipe.core.util.ProxyManagerProvider;
    uses io.xpipe.core.util.DataStateProvider;
    uses ModuleLayerLoader;
    uses ShellDialect;

    provides ModuleLayerLoader with
            ShellDialects.Loader;
    provides com.fasterxml.jackson.databind.Module with
            CoreJacksonModule;
}
