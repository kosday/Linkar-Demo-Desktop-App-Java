package bl;

import linkar.functions.DBMV_Mark;

//Helper class for IMPORT / EXPORT QM string to Class
public class LinkarDataTypes {
	
    // CONVERSIONS

    public static String ConvertToHTML(String value, boolean replaceMV, boolean replaceSV)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < value.length(); i++)
        {
            if (value.charAt(i) == '\r')
            {
                int pos2 = i + 1;
                if (pos2 < value.length())
                {
                    if (value.charAt(pos2) == '\n')
                    {
                        i++;
                        sb.append((char)251);
                    }
                    else
                        sb.append((char)251);
                }
            }
            else if (value.charAt(i) == '\n')
                sb.append((char)251);
            else switch (value.charAt(i))
                {
                    case (char)255: sb.append("&yuml;"); break;
                    case (char)254: sb.append("&thorn;"); break;
                    case (char)253:
                        if (replaceMV)
                            sb.append("&yacute;");
                        else
                            sb.append(value.charAt(i));
                        break;
                    case '€': sb.append("&euro;"); break;
                    default:
                        if (value.charAt(i) >= 32 && value.charAt(i) < 253)
                            sb.append(value.charAt(i));
                        break;
                }

        }

        String result = sb.toString();
        return result;
    }

    public static String ConvertFromHTML(String value, boolean replaceMV, boolean replaceSV)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < value.length(); i++)
        {
            if (value.charAt(i) == '&')
            {
                int len = 7;
                if (i + 7 > value.length())
                    len = value.length() - i;
                String token = value.substring(i, len);
                if (token.startsWith("&yuml;"))
                {
                    sb.append((char)255);
                    i += 5;
                }
                else if (token.startsWith("&thorn;"))
                {
                    sb.append((char)254);
                    i += 6;
                }
                else if (token.startsWith("&euro;"))
                {
                    sb.append("€");
                    i += 5;
                }
                else if (replaceMV && token.startsWith("&yacute;"))
                {
                    sb.append((char)253);
                    i += 7;
                }
                else
                {
                    sb.append(value.charAt(i));
                }
            }
            else if (value.charAt(i) == (char)251)
                sb.append("\r\n");
            else
                sb.append(value.charAt(i));
        }

        String result = sb.toString();
        return result;
    }

    //--

    //#region RAW
    public static String GetRaw(String rawValue, int posVM, int posSM)
    {
        return GetValueText(rawValue, posVM, posSM);
    }

    public static String SetRaw(String rawValue, String realValue, int posVM, int posSM)
    {
        if (realValue != null && realValue != "")
            rawValue = SetValueText(rawValue, realValue, posVM, posSM);
        else
        	rawValue = SetValueText(rawValue, "", posVM, posSM);
        return rawValue;
    }


    //--

    // ALPHA
    public static String GetAlpha(String rawValue, int posVM, int posSM)
    {
        String realValue = ConvertFromHTML(GetValueText(rawValue, posVM, posSM), (posVM > -1), (posSM > -1));
        return realValue;
    }

    public static String SetAlpha(String rawValue, String realValue, int posVM, int posSM)
    {
        if (realValue != null && realValue != "")
        	rawValue = SetValueText(rawValue, ConvertToHTML(realValue, posVM > -1, posSM > -1), posVM, posSM);
        else
        	rawValue = SetValueText(rawValue, "", posVM, posSM);
        return rawValue;
    }

    //--

    // INTEGER
    public static int GetInteger(String rawValue, int posVM, int posSM)
    {
        String realValue = GetValueText(rawValue, posVM, posSM);
        int response = 0;
        if (realValue != null && realValue != "")
        {
        	try {
        		response = Integer.valueOf(realValue);
        	}
        	catch(Exception ex){
        		response = 0;
        	}
        }
        return response;
    }

    public static String SetInteger(String rawValue, int realValue, int posVM, int posSM)
    {
        return SetValueText(rawValue, String.valueOf(realValue), posVM, posSM);
    }
    
    //--

    // BOOL
    
    public static boolean GetBool(String rawValue, String propBoolTrue, int posVM, int posSM)
    {
        return GetValueText(rawValue, posVM, posSM) == propBoolTrue ? true : false;
    }

    public static String SetBool(String rawValue, boolean realValue, String propBoolTrue, String propBoolFalse, int posVM, int posSM)
    {
        return SetValueText(rawValue, realValue ? propBoolTrue : propBoolFalse, posVM, posSM);
    }
    
    //--
    
    // DECIMAL
    public static double GetDecimal(String rawValue, int posVM, int posSM)
    {
        String realValue = GetValueText(rawValue, posVM, posSM);
        double response = 0.0;
        if (realValue != null && realValue != "")
        {
        	try {
        		response = Double.valueOf(realValue);
        	}
        	catch(Exception ex){
        		response = 0.0;
        	}
        }
        return response;
    }

    public static String SetDecimal(String rawValue, double realValue, int decimallength, int posVM, int posSM)
    {
        if (decimallength < 0)
        	rawValue = SetValueText(rawValue, String.valueOf(realValue), posVM, posSM);
        else
        	rawValue = SetValueText(rawValue, String.valueOf(Math.round(realValue * (10^decimallength)) / (10^decimallength)), posVM, posSM);
        return rawValue;
    }
    //--

    // DATETIME

    public static String GetDateTime(String rawValue, int posVM, int posSM)
    {
        return GetValueText(rawValue, posVM, posSM);
    }

    public static String SetDateTime(String rawValue, String realValue, int posVM, int posSM)
    {
        return SetValueText(rawValue, realValue, posVM, posSM);
    }

    //--
    
    // GET_METHODS

    private static String GetValueMark(String text, char mark, int pos)
    {
        String[] array = text.split(String.valueOf(mark));
        if (pos < array.length && pos != -1)
            return array[pos];
        else
            return "";
    }

    private static String GetValueText(String text, int posVM, int posSM)
    {
        String response = "";
        if (posVM == -1 && posSM == -1)
            response = text;
        else if (posVM != -1 && posSM == -1)
            response = GetValueVM(text, posVM);
        else
            response = GetValueSM(text, posVM, posSM);

        return response;
    }

    private static String GetValueVM(String text, int pos)
    {
        return GetValueMark(text, DBMV_Mark.VM, pos);
    }

    private static String GetValueSM(String text, int posVM, int posSM)
    {
        String textVM = GetValueVM(text, posVM);
        if (textVM != "")
            return GetValueMark(textVM, DBMV_Mark.SM, posSM);
        else
            return textVM;
    }

    //--

    // SET_METHODS

    private static String SetValueMark(String returnText, String text, char mark, int pos)
    {
        if (text != null && text != "")
            returnText += (pos > 0 ? String.valueOf(mark) : "") + text;
        else
            returnText += (pos > 0 ? String.valueOf(mark) : "");
        return returnText;
    }

    private static String SetValueText(String returnText, String text, int posVM, int posSM)
    {
        if (posVM == -1 && posSM == -1)
            returnText = text;
        else if (posVM != -1 && posSM == -1)
        	returnText = SetValueVM(returnText, text, posVM);
        else
        	returnText = SetValueSM(returnText, text, posVM, posSM);
        return returnText;
    }

    private static String SetValueVM(String returnText, String text, int pos)
    {
        return SetValueMark(returnText, text, DBMV_Mark.VM, pos);
    }

    private static String SetValueSM(String returnText, String text, int posVM, int posSM)
    {
    	returnText = SetValueVM(returnText, text, posVM);
        if (returnText != "")
        	returnText = SetValueMark(returnText, returnText, DBMV_Mark.SM, posSM);
        return returnText;
    }

    //--
}
