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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * @author Ivan Gadzhega
 * @since 0.1
 */
public class GameInputProcessor extends InputAdapter {

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                return true;
        }
        return super.keyDown(keycode);
    }

}
