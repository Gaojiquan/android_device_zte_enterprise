package com.android.internal.telephony;

import static com.android.internal.telephony.RILConstants.*;

import android.content.Context;
import android.os.Message;
import android.os.Parcel;

import android.util.Log;

import android.text.TextUtils;

import android.telephony.SignalStrength;
import android.telephony.PhoneNumberUtils;

import java.util.ArrayList;
import java.util.Collections;

public class EnterpriseRIL extends RIL implements CommandsInterface {

    public EnterpriseRIL(Context context, int networkMode, int cdmaSubscription) {
        super(context, networkMode, cdmaSubscription);
    }

    @Override
    protected void
    processUnsolicited (Parcel p) {
        Object ret;
        int dataPosition = p.dataPosition(); // save off position within the Parcel
        int response = p.readInt();

        switch(response) {
            case RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED: ret =  responseVoid(p); break;
            default:
                // Rewind the Parcel
                p.setDataPosition(dataPosition);

                // Forward responses that we are not overriding to the super class
                super.processUnsolicited(p);
                return;
        }
        switch(response) {
            case RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED:
                /* has bonus radio state int */
                RadioState newState = getRadioStateFromInt(p.readInt());
                p.setDataPosition(dataPosition);
                super.processUnsolicited(p);
                if (RadioState.RADIO_ON == newState) {
                    //setNetworkSelectionModeAutomatic(null);
                }
                return;
        }
    }

	/** From stock RIL:
	 *-----------------------------------
	 *	OperatorInfo :
	 *		this.operatorAlphaLong = paramString1;
	 *		this.operatorAlphaShort = paramString2;
	 *		this.operatorNumeric = paramString3;
	 *		this.state = paramState;
	 *		this.rat = paramRadioAccessTechnology;
	 */
	@Override
	protected Object responseOperatorInfos(Parcel p) {
		String[] strings = (String[]) responseStrings(p);
		if (strings.length % 5 != 0)
			throw new RuntimeException(
					"RIL_REQUEST_QUERY_AVAILABLE_NETWORKS: invalid response. Got "
							+ strings.length
							+ " strings, expected multible of 5");
		ArrayList ret = new ArrayList(strings.length / 5);

		for (int i = 0 ; i < strings.length ; i += 5) {
			riljLog("responseOperatorInfos plmn = " + strings[(i + 0)]);
			if ((strings[(i + 0)] != null)
					&& !TextUtils.isEmpty(strings[(i + 0)])) {

				riljLog("responseOperatorInfos number = "
						+ strings[(i + 2)]);
				riljLog("responseOperatorInfos RadioAccessTechnology = "
						+ strings[(i + 4)]);

				ret.add(new OperatorInfo(
						/*getPLMN(strings[(i + 2)]),*/
						/* here we use plmn directory */
						strings[(i + 0)] + ", " + strings[(i + 4)],
						strings[(i + 1)], 
						strings[(i + 2)],
						strings[(i + 3)]/*, RadioAccessTechnology:
						strings[(i + 4)]*/));
			}
		}

		return ret;
	}
	
	@Override
	public void setNetworkSelectionModeManual(String operatorNumeric,
			Message response) {

		RILRequest rr = RILRequest.obtain(RIL_REQUEST_SET_NETWORK_SELECTION_MANUAL, response);
		riljLog(rr.serialString() + "> "
				+ requestToString(rr.mRequest) + " " + operatorNumeric);
		rr.mp.writeInt(1);
		rr.mp.writeString(operatorNumeric);
		send(rr);

	}

    @Override
    protected Object responseSignalStrength(Parcel p) {
        int numInts = 12;
        int response[];

        // This is a mashup of algorithms used in
        // SamsungQualcommUiccRIL.java

        // Get raw data
        response = new int[numInts];
        for (int i = 0; i < numInts; i++) {
            response[i] = p.readInt();
        }

		// response[1]=0 -- gsm
		// response[1]=99 -- 3G

		/*
		 * 	[signalStrength=137, 			-- 		response[0]
		 *	 bitErrorRate=99,    			-- 		response[1]	            
		 *	 CDMA_SS.dbm=-1,				-- 		response[2]
		 *	 CDMA_SSecio=-1,    			-- 		response[3]            			
		 *	 EVDO_SS.dbm=-1,				-- 		response[4]
		 *	 EVDO_SS.ecio=-1,   			-- 		response[5]             
		 *	 EVDO_SS.signalNoiseRatio=-1,   -- 		response[6]             
		 *	 LTE_SS.signalStrength=-1,		-- 		response[7]
		 *	 LTE_SS.rsrp=-1,				-- 		response[8]
		 *	 LTE_SS.rsrq=-1,                -- 		response[9]
		 *	 LTE_SS.rssnr=-1,				-- 		response[10]
		 *	 LTE_SS.cqi=-1]					-- 		response[11]
		 */
	
		int gsmSignalStrength = response[0];

		// if is TD signal, do a convert
		if (gsmSignalStrength >= 100) {

		  if (gsmSignalStrength % 2 == 0) {

		  	if (gsmSignalStrength <= 165) {

				if (gsmSignalStrength > 103)
					gsmSignalStrength++;
				else
					gsmSignalStrength = 103;

			} else {
				gsmSignalStrength = 165;
			}
		  }

		  gsmSignalStrength = gsmSignalStrength - 103 >> 1;
		  response[0] = gsmSignalStrength;
		}

        // RIL_LTE_SignalStrength
        if (response[7] == 99) {
            // If LTE is not enabled, clear LTE results
            // 7-11 must be -1 for GSM signal strength to be used (see
            // frameworks/base/telephony/java/android/telephony/SignalStrength.java)
            response[8] = -1;
            response[9] = -1;
            response[10] = -1;
            response[11] = -1;
        }

		boolean isGsm = (mPhoneType == RILConstants.GSM_PHONE);

        return new SignalStrength(response[0], 
			response[1], response[2], response[3], response[4], response[5], 
			response[6], response[7], response[8], response[9], response[10], response[11], isGsm);

    }

}
