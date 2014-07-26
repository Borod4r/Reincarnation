/*
 * Copyright (c) 2014 Ivan Gadzhega.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Ivan Gadzhega.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with Ivan Gadzhega.
 */

package net.ivang.reincarnation;

import com.badlogic.gdx.math.Vector3;

/**
 * @author Ivan Gadzhega
 * @since 0.1
 */
public class Voxel {

    private int mDepth;
    private float mSize;
    private Vector3 mOrigin;
    private Voxel[] mChildren;

    //---------------------------------------------------------------------
    // Constructors
    //---------------------------------------------------------------------

    public Voxel(Vector3 origin, float size, int depth) {
        mOrigin = origin;
        mSize = size;
        mDepth = depth;
    }

    //---------------------------------------------------------------------
    // Public
    //---------------------------------------------------------------------

    public void createChildren() {
        mChildren = new Voxel[8];

        int childDepth = mDepth + 1;
        float childSize = mSize / 2;
        float shift = childSize / 2;

        for (Octants octant : Octants.values()) {
            Vector3 childOrigin = mOrigin.cpy();
            childOrigin.mulAdd(octant.getSigns(), shift);
            int childOrdinal = octant.ordinal();
            mChildren[childOrdinal] = new Voxel(childOrigin, childSize, childDepth);
        }
    }

    public boolean isLeaf() {
        return mChildren == null;
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public float getSize() {
        return mSize;
    }

    public Vector3 getOrigin() {
        return mOrigin;
    }

    public Voxel[] getChildren() {
        return mChildren;
    }


    //---------------------------------------------------------------------
    // Nested Classes
    //---------------------------------------------------------------------

    /**
     * Octants follow a predictable pattern to make accesses simple.
     * Here, - means less than 'origin' in that dimension, + means greater than.
     *
     * Child:	0 1 2 3 4 5 6 7
     * x:       + + + + - - - -
     * y:       + + - - + + - -
     * z:       + - + - + - + -
     *
     * {@see http://howie.gse.buffalo.edu/effilno/interests/math/octants/}
     */
    private enum Octants {
        RIGHT_TOP_FRONT     ( 1,  1,  1),
        RIGHT_TOP_BACK      ( 1,  1, -1),
        RIGHT_BOTTOM_FRONT  ( 1, -1,  1),
        RIGHT_BOTTOM_BACK   ( 1, -1, -1),
        LEFT_TOP_FRONT      (-1,  1,  1),
        LEFT_TOP_BACK       (-1,  1, -1),
        LEFT_BOTTOM_FRONT   (-1, -1,  1),
        LEFT_BOTTOM_BACK    (-1, -1, -1);

        private final Vector3 signs;

        private Octants(float x, float y, float z) {
            this.signs = new Vector3(x, y, z);
        }

        public Vector3 getSigns() {
            return signs;
        }
    }
}
