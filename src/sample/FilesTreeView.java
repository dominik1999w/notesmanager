package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;

class FilesTreeView {
    TreeItem<File> createNode(final File file) {
        return new TreeItem<File>(file) {
            private boolean FirstChildren = true;
            private boolean Leaf;
            @Override
            public ObservableList<TreeItem<File>> getChildren(){ //getChildren for a specific node
                if (FirstChildren) {
                    FirstChildren = false;
                    super.getChildren().setAll(createRootsChildren(this));
                }
                return super.getChildren();
            }
            @Override
            public boolean isLeaf() {
                File f = getValue();
                Leaf = f.isFile();
                return Leaf;
            }
            private ObservableList<TreeItem<File>> createRootsChildren(TreeItem<File> root) {
                File f = root.getValue();
                if (f != null && f.isDirectory()) {
                    File[] files = f.listFiles();
                    if (files != null) {
                        ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
                        for (File childFile : files) {
                            children.add(createNode(childFile));
                        }
                        return children;
                    }
                }
                return FXCollections.emptyObservableList();
            }
        };
    }
}
