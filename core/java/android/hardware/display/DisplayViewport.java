/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.hardware.display;

import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Surface;
import android.view.Display;

/**
 * Describes how the pixels of physical display device reflects the content of
 * a logical display.
 * <p>
 * This information is used by the input system to translate touch input from
 * physical display coordinates into logical display coordinates.
 * </p>
 *
 * @hide Only for use within the system server.
 */
public final class DisplayViewport {
    // True if this viewport is valid.
    public boolean valid;

    // The logical display id.
    public int displayId;

    // The rotation applied to the physical coordinate system.
    public int orientation;

    // The portion of the logical display that are presented on this physical display.
    public final Rect logicalFrame = new Rect();

    // The portion of the (rotated) physical display that shows the logical display contents.
    // The relation between logical and physical frame defines how the coordinate system
    // should be scaled or translated after rotation.
    public final Rect physicalFrame = new Rect();

    // The full width and height of the display device, rotated in the same
    // manner as physicalFrame.  This expresses the full native size of the display device.
    // The physical frame should usually fit within this area.
    public int deviceWidth;
    public int deviceHeight;

    // The ID used to uniquely identify this display.
    public String uniqueId;

    public void copyFrom(DisplayViewport viewport) {
        valid = viewport.valid;
        displayId = viewport.displayId;
        orientation = viewport.orientation;
        logicalFrame.set(viewport.logicalFrame);
        physicalFrame.set(viewport.physicalFrame);

        uniqueId = viewport.uniqueId;
        
        int signBoardHeight = 160;
        
        if (displayId == Display.DEFAULT_DISPLAY) {
            if (orientation == Surface.ROTATION_0) {
                physicalFrame.top += signBoardHeight;
                physicalFrame.bottom += signBoardHeight;
            } else if (orientation == Surface.ROTATION_90) {
                physicalFrame.left += signBoardHeight;
                physicalFrame.right += signBoardHeight;
            }
        }

        deviceWidth = viewport.deviceWidth;
        deviceHeight = viewport.deviceHeight;
    }

    /**
     * Creates a copy of this DisplayViewport.
     */
    public DisplayViewport makeCopy() {
        DisplayViewport dv = new DisplayViewport();
        dv.copyFrom(this);
        return dv;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof DisplayViewport)) {
            return false;
        }

        DisplayViewport other = (DisplayViewport) o;
        return valid == other.valid
              && displayId == other.displayId
              && orientation == other.orientation
              && logicalFrame.equals(other.logicalFrame)
              && physicalFrame.equals(other.physicalFrame)
              && deviceWidth == other.deviceWidth
              && deviceHeight == other.deviceHeight
              && TextUtils.equals(uniqueId, other.uniqueId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result += prime * result + (valid ? 1 : 0);
        result += prime * result + displayId;
        result += prime * result + orientation;
        result += prime * result + logicalFrame.hashCode();
        result += prime * result + physicalFrame.hashCode();
        result += prime * result + deviceWidth;
        result += prime * result + deviceHeight;
        result += prime * result + uniqueId.hashCode();
        return result;
    }

    // For debugging purposes.
    @Override
    public String toString() {
        return "DisplayViewport{valid=" + valid
                + ", displayId=" + displayId
                + ", uniqueId='" + uniqueId + "'"
                + ", orientation=" + orientation
                + ", logicalFrame=" + logicalFrame
                + ", physicalFrame=" + physicalFrame
                + ", deviceWidth=" + deviceWidth
                + ", deviceHeight=" + deviceHeight
                + "}";
    }
}
