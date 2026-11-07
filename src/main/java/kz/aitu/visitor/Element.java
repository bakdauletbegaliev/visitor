package kz.aitu.visitor;

import java.util.List;

public interface Element {
    String getName();
    List<? extends Element> getChildren();
    void accept(Visitor visitor);
}
