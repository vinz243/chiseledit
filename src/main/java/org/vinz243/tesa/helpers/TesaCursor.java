package org.vinz243.tesa.helpers;

import org.vinz243.tesa.Tessellator;
import org.vinz243.tesa.annotations.CursorTarget;
import org.vinz243.tesa.transforms.Transform;

public class TesaCursor {
    private Transform currentTransform;
    private Tessellator currentTessellator;

    public TesaCursor(Transform currentTransform, Tessellator currentTessellator) {
        this.currentTransform = currentTransform;
        this.currentTessellator = currentTessellator;
    }

    public TesaCursor() {
    }

    @CursorTarget(name = {"t", "transform", "tf"})
    public Transform getCurrentTransform() {
        return currentTransform;
    }

    public void setCurrentTransform(Transform currentTransform) {
        this.currentTransform = currentTransform;
    }

    @CursorTarget(name = {"g", "tessellator", "tess", "global"})
    public Tessellator getCurrentTessellator() {
        return currentTessellator;
    }

    public void setCurrentTessellator(Tessellator currentTessellator) {
        this.currentTessellator = currentTessellator;
    }

    //private Group currentGroup;
}
