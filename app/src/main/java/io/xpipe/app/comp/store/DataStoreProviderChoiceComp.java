package io.xpipe.app.comp.store;

import io.xpipe.app.ext.DataStoreProvider;
import io.xpipe.app.ext.DataStoreProviders;
import io.xpipe.app.fxcomps.Comp;
import io.xpipe.app.fxcomps.CompStructure;
import io.xpipe.app.fxcomps.SimpleCompStructure;
import io.xpipe.app.prefs.AppPrefs;
import io.xpipe.app.util.JfxHelper;
import javafx.beans.property.Property;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DataStoreProviderChoiceComp extends Comp<CompStructure<ComboBox<DataStoreProvider>>> {

    Predicate<DataStoreProvider> filter;
    Property<DataStoreProvider> provider;
    boolean staticDisplay;

    private List<DataStoreProvider> getProviders() {
        return DataStoreProviders.getAll().stream().filter(filter).toList();
    }

    private Region createGraphic(DataStoreProvider provider) {
        if (provider == null) {
            return null;
        }

        var graphic = provider.getDisplayIconFileName(null);
        return JfxHelper.createNamedEntry(provider.getDisplayName(), provider.getDisplayDescription(), graphic);
    }

    @Override
    public CompStructure<ComboBox<DataStoreProvider>> createBase() {
        Supplier<ListCell<DataStoreProvider>> cellFactory = () -> new ListCell<>() {
            @Override
            protected void updateItem(DataStoreProvider item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(createGraphic(item));
                setAccessibleText(item != null ? item.getDisplayName() : null);
                setAccessibleHelp(item != null ? item.getDisplayDescription() : null);
            }
        };
        var cb = new ComboBox<DataStoreProvider>();
        cb.setCellFactory(param -> {
            return cellFactory.get();
        });
        cb.setButtonCell(cellFactory.get());
        var l = getProviders().stream()
                .filter(p -> AppPrefs.get().developerShowHiddenProviders().get() || p.getCreationCategory() != null || staticDisplay)
                .toList();
        l.forEach(dataStoreProvider -> cb.getItems().add(dataStoreProvider));
        if (provider.getValue() == null) {
            provider.setValue(l.getFirst());
        }
        cb.setValue(provider.getValue());
        provider.bind(cb.valueProperty());
        cb.getStyleClass().add("choice-comp");
        cb.setAccessibleText("Choose connection type");
        cb.setOnKeyPressed(event -> {
            if (!event.getCode().equals(KeyCode.ENTER)) {
                return;
            }

            cb.show();
            event.consume();
        });
        return new SimpleCompStructure<>(cb);
    }
}
