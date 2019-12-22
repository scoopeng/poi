/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hssf.record;

import java.util.Arrays;
import java.util.Locale;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.Removal;

/**
 * The HEADERFOOTER record stores information added in Office Excel 2007 for headers/footers.
 */
public final class HeaderFooterRecord extends StandardRecord {
    public static final short sid = 0x089C;
    private static final byte[] BLANK_GUID = new byte[16];

	private byte[] _rawData;

    public HeaderFooterRecord(byte[] data) {
        _rawData = data;
    }

    public HeaderFooterRecord(HeaderFooterRecord other) {
        super(other);
        _rawData = (other._rawData == null) ? null : other._rawData.clone();
    }

	/**
	 * construct a HeaderFooterRecord record.  No fields are interpreted and the record will
	 * be serialized in its original form more or less
	 * @param in the RecordInputstream to read the record from
	 */
	public HeaderFooterRecord(RecordInputStream in) {
		_rawData = in.readRemainder();
	}

	/**
	 * spit the record out AS IS. no interpretation or identification
	 */
	public void serialize(LittleEndianOutput out) {
		out.write(_rawData);
	}

	protected int getDataSize() {
		return _rawData.length;
	}

    public short getSid()
    {
        return sid;
    }

    /**
     * If this header belongs to a specific sheet view , the sheet view?s GUID will be saved here.
     * <p>
     * If it is zero, it means the current sheet. Otherwise, this field MUST match the guid field
     * of the preceding {@link UserSViewBegin} record.
     *
     * @return the sheet view?s GUID
     */
    public byte[] getGuid(){
        byte[] guid = new byte[16];
        System.arraycopy(_rawData, 12, guid, 0, guid.length);
        return guid;
    }

    /**
     * @return whether this record belongs to the current sheet
     */
    public boolean isCurrentSheet(){
        return Arrays.equals(getGuid(), BLANK_GUID);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('[').append("HEADERFOOTER").append("] (0x");
        sb.append(Integer.toHexString(sid).toUpperCase(Locale.ROOT)).append(")\n");
        sb.append("  rawData=").append(HexDump.toHex(_rawData)).append("\n");
        sb.append("[/").append("HEADERFOOTER").append("]\n");
        return sb.toString();
    }

    @Override
    @SuppressWarnings("squid:S2975")
    @Deprecated
    @Removal(version = "5.0.0")
    public HeaderFooterRecord clone() {
        return copy();
    }

    @Override
    public HeaderFooterRecord copy() {
        return new HeaderFooterRecord(this);
    }


}
