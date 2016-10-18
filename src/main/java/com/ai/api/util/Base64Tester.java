package com.ai.api.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.ai.commons.JsonUtil;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@SuppressWarnings("restriction")
public class Base64Tester {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// String path = "E:/test.docx";
		// String path = "E:/test.xlsx";
		// String path = "E:/test.pdf";
//		String path = "E:/test.xls";
//		File file = new File(path);
//		FileInputStream inputFile = new FileInputStream(file);
//		byte[] buffer = new byte[(int) file.length()];
//		inputFile.read(buffer);
//		inputFile.close();
//		String result = Base64.encode(buffer);
		
//		String resultStr = "[{orderId=D61D546821E747058386B909F7248E65, serviceType=1, serviceTypeText=PSI, status=40, statusText=test, supplierName=test, refNumber=R-Cloud-1602137, clientReference=222, productNames=red light, poNumbers=asfas, serviceDate=19-Oct-2016, bookingDate=10-Oct-2016, confirmBySupplier=null, isSupplierConfirmed=false}]";
//		List<SimpleOrderSearchBean> tempList = JsonUtil.mapToObject(resultStr, new TypeReference<List<SimpleOrderSearchBean>>(){});

		String str = "UEsDBBQACAgIADJUUkkAAAAAAAAAAAAAAAARAAAAZG9jUHJvcHMvY29yZS54bWytkV1LwzAUhu/7K0Lu2yQtjhHaDlEGguLAisO7kB7bYvNBEu3892bdrCheennyPu/D4aTcHNSI3sH5wegKs4xiBFqadtBdhR+bbbrGmzpJSmkc7Jyx4MIAHsWW9hXuQ7CcEC97UMJnMdYxeTFOiRBH1xEr5KvogOSUroiCIFoRBDnaUrvo8MnHpf13ZSsXpX1z4yxoJYERFOjgCcsY+WYDOOX/LMzJQh78sFDTNGVTMXNxI0b2d7cP8/LpoH0QWgKuE4TKs51LByJAi6KDhw8LFf5Knoqr62aL65yyVcpoytYNzXlR8OLiuSS/+mfnaTSuvoxn6QHt7m+O6PKclOTn19XJJ1BLBwhuGg+SBAEAAPQBAABQSwMEFAAICAgAMlRSSQAAAAAAAAAAAAAAAAsAAABfcmVscy8ucmVsc62SwU7DMAyG732KKPfV3ZAQQk13QUi7TWg8QEjcNmoTR4kH5e0JBwRDDHbgGOf350+y2+3iZ/GMKTsKSq7rRgoMhqwLg5KPh/vVjdx2VdU+4Ky5ZPLoYhalKWQlR+Z4C5DNiF7nmiKG8tNT8prLMw0QtZn0gLBpmmtIXxmyq4Q4wYqdVTLt7FqKw2vES/DU987gHZmjx8A/TPmWKGSdBmQllxleKE1PRFNdoBLO6mz+UwcXxmDRrmIq/Ykd5k8nS2Zfyhl0jH9IXV0udX4F4JG11azBUMLfld4TH04tnFxDV70BUEsHCFcoXiPjAAAARgIAAFBLAwQUAAgICAAyVFJJAAAAAAAAAAAAAAAAEwAAAFtDb250ZW50X1R5cGVzXS54bWytlMlOwzAURff5CstblLhlgRBK2wXDEipRPsDYL4nVeJDtTn/Pc9oCreiA2pUTv3vvubGslKOlbskcfFDWDGi/6FECRlipTD2gH5OX/J6OhllWTlYOAkGxCQPaxOgeGAuiAc1DYR0YnFTWax7x1dfMcTHlNbDbXu+OCWsimJjHlEGHGSHlE1R81kbyvMTJmu1MTcnjWppoA6p0ikj77JDJQxv2XNy5Vgkecc7mRu61yzfNCnR2mtAoF25QcBiShocZP9Y3PEevJJAx9/GVaxQyacXYWxcYWorjQX+UtVWlBGDGTKOlgNRJgswdRoKPCn43P4oX1sP/+dvDSu7zocuWSc8XeIXC9qF/8bdvgk6jQ8M9yPfoE/9ibHAeuAwNQNRtsZN9RpW4auHqHbrQ0/CF9dNPa6dXPwJcC82VOa9Cpw+sWy6/BbtdvvO3VUrW/aeG2RdQSwcI8TzspFMBAADYBAAAUEsDBBQACAgIADJUUkkAAAAAAAAAAAAAAAAQAAAAZG9jUHJvcHMvYXBwLnhtbE2OwQrCMBBE74L/EHJvt3oQkTSlIIIne9APCOnWBppNSFbp55uTepwZ5vFUt/pFvDFlF6iVu7qRAsmG0dGzlY/7pTrKTm83akghYmKHWZQH5VbOzPEEkO2M3uS6zFSWKSRvuMT0hDBNzuI52JdHYtg3zQFwZaQRxyp+gVKrPsbFWcNFQvfRFKQYblcF/72Cn4P+AFBLBwg2boMhkwAAALgAAABQSwMEFAAICAgAMlRSSQAAAAAAAAAAAAAAABgAAAB4bC9kcmF3aW5ncy9kcmF3aW5nMS54bWydU9tu2zAMfR+wfxD0vspxkyY14hRBgw4Dii0Y1g9QZToWpotBKYn396Nie0mwPAx7sclD8pwjQlo+ddawA2DQ3pV8cpdxBk75Srtdyd9+vHxa8KfVxw/LrsLiGDbIqN+FgtKSNzG2hRBBNWBluPMtOKrWHq2MlOJOVCiPxGSNyLPsQYQWQVahAYibvsIHPvkfbFZqN87/kxtf11rBxqu9BRd7EgQjI509NLoNfHU6Zzz6ZzBm7VTjkUGl4zqUfECHnhq97SPlzSpbijEcsW91fQGn7FRBfxzhFI7YRfeQ9clZJvqz3PS2XP44y+eL25r5bc1JNp3PZzeER7lWqz5wh61WWxwEvx62yHRFN4YzJy2UnKpxj8AIqCAo/INwcZ7pGWRiffXqZ2DOPzfS7WAdWlCRloz704C4nhB/OXg3un3RxiS2FDMswL4DOcIv1SRRyCJEhKiaFNbU+p0UEvVFQVxTpSy0vcWuRpv+dGlYV3J6F7/S90QMXWSKwHx+f/8wn3GmqDZdzGiVvFcYp1sM8TN4y1JA3sgCT7g8vIbBzNgyuOn1xdXuldF0YTcyynE1V1d0wNLzXP0GUEsHCFzWbGG6AQAA0QMAAFBLAwQUAAgICAAyVFJJAAAAAAAAAAAAAAAAIwAAAHhsL2RyYXdpbmdzL19yZWxzL2RyYXdpbmcxLnhtbC5yZWxzjY/LCsIwEEX3+YowezOtCxFp6kaEbqV+QEinadA8SKLo3xtwo+DC5czcew7T7R/uyu+Usg1eQisa4OR1mKw3Es7jcbWFfc9Yd6KrKjWTFxszryWfJSylxB1i1gs5lUWI5OtlDsmpUsdkMCp9UYZw3TQbTJ8M6BnnX1g+TBLSMLXAx2ekf/Bhnq2mQ9A3R778sKB1VV+BKhkqEoRAR5NV730rojeAPevw67+evQBQSwcIKbETAK0AAAAYAQAAUEsDBBQACAgIADJUUkkAAAAAAAAAAAAAAAATAAAAeGwvbWVkaWEvaW1hZ2UxLnBuZwFvEpDtiVBORw0KGgoAAAANSUhEUgAAAR8AAAAzCAYAAACuTgy0AAASNklEQVR4nO2df4wU5RnHRyNYKXAENHAKXEiBVM8ejUp6qFwTgxalkGoVtIGkmkDbiMFEriaK/IFoYg8TjZi2mNY2R0oPfyVQqVLapFD1UNTelYMKEnNABVMx3GExck3a/by7z/LsezM77+zu7d4t7zeZzM7M++OZed/n+z7P874ze97/Ugg8PDw8yowL7BMn3mwPPnru+eD4a9srIc+AYnT9FcGslzcFw0aPrrQoHh7nPM7Tlk/H/c3BkbYXKynPgGHEpInB7B2veuLx8BgkOF9+dK1+tGqJB1zz/C898Xh4DCIY8km7Wr+utCwDhvo1jxiXy8PDY/DAkA8xnmrFuGsbgylL73FOf+LEiaC1tdVsbW1tAyhZ8fhD7ZTQ8+vXPxPUThhvNg+PwQpDPtUYXBbMeKolUfru7u7s7zNnzuQce3h4lA7nxycZupj+wAoTaE4CIZu6ujqzP3bsWMnl8vDwqGLyIbg8ZZm7uwVwuT7//PNg+PDhWfLxlo+Hx8CgaslnytK7E89uCdHU1taaDeB6QUoeHh7J8dlnnwX79+83g7qNqiWfiYtuT5xHkw/WjxCQt348PJIDwtm+fXtw6NChYM+ePf2u91vhXA2YlCKepLEecbmAkA57Yj6Qz1VXXRWZl3x79+416bCUxo0bF0yYMCGYPn16MHLkyGy6rq6u4L333jO/lyxZklMGeamLkQJZID/KQYZp06aZ41JAZsD++cEHwcmTPcHG1tZg69Ytpv6GhhnB/AXzg+XL7wvNS7qtW7aafU3NmKCpqclsi9W9dHZ2BI+tXRvs3LkzJy/pZ8xoCGan0nPvHIehp+dksP6Z9VmZSIdM5EG+fOBeqJd8yKHr5J6YBUS2pDh2/BOz1/nlnA3qR/bOjk4jA+47ctvPSSBlIt/Dq1Zl70Geseu9D0agC/Rb2WwURD6smRlWk3ZpevfuC/p6e4uTssQYP/fGxHmOHz9u9ii8EAYdB7KAXCAErtng/I4dO8yDJh9pOMdGfk0+UTh48GDQ3t6erR/COXXqlCEjIb85c+aUjIAASwlQcpQdxQAoi9lSirPhuedy0qMUzc0rzW9RBBQEaKWC0IR4UCZBT+o86XemFI16N7+wuZ9CUffCOxYamXh2yMW9UzdbS8u6UAUmX/PKZrMHkldkmZyJ3w0kkHlt6t6QU8uA/Ia0MxvPNYx4zT2kni/5JS+yy72/nrIghhoBjR07Npg0aVLQ19cXjB/ff9lHIvKZvvJ+Y1VcNPGynPNHN78UHFj3VHD6yNHIvFgiTTu2BReMHpU919u1P9g555bIPKQfXX+5U1oBcZ4Jc2+KTWfDnuUCmky4HkY+77//viEerJPGxsac8sLSh4G8ei+AeOiA1A9B1dfXJ76vKDDaosirUgQhyiCjMEqysfXsSC2KBTQBcI9ci4JtQS2/b7khCcpnj0IJKEeIxyYZkQsZsGJ0G2nCQjkhPCFTKRcSAvPnLwhV4EUL7zB76iRNIRDi4Vm2rGvJKUful7ZctnRp0Lb5hX75uUZeron8PF/Sc4/s23e/XZBslcTMmTMjrzmRDwo96+XfGyL44ui/zKsYvV370op+803BxIXfNwr/5m13mvNhIAajiQdQHosAWWEdBiEe+3c+IE9SiGUD6qxREvdJyCfM9ZKpeDuffRwHm3iAuFy4a9RfSvJBPk08ALLAraKzozBCAB0pS0hIRpNC+h7d75O6IAfjlmSsLCEDscIo37ZutFwouLaoICWxlLCmbKuCYzlHmnztIhZHUoiFArBs7DIgImSA5CRtmAUHOeu8yMM5iIf2J6+LfKRlsMJ6pm8zCFKW7j+Shr4tVjthAp3mlVdeMXvqZJCVcABloQtSBjoQlj8OTgHnGU+vM8p/4Mmngz/PvN68igFhsDjx7ytWpiySeSbdtSmCioq1QDJhKMRKyYexs76VOI9YPdrlEkhn1QSlIelpBBqxWFAHZIMbhjsnspV6xk0Uot/5BfPNHsIRaIUVJSsUuiyxSEBrplw7FmbLpWNJopAAgoqKIw00xP2U2E4YOC9Ea8fDBGH3ri0ocSvzgb5D+fRFyKApYylql13ScA4SwWKn7xNiEPdfg7S4UKS78MILs/2TjQFSzpMfPXBFrOUDaUyYe2PWtQoD1g5WT9OOV41rBiFpYCGNiyCFKFIqFIWUJwr+5ZdfGoXPl852pWg8CXJCEDS4a6zHLlviS+VAzZiavNe1O2VG4EzAlriEWEWFuChagQgGyzmpLyogLG2k8+vfQk6VQGeGqOOsEq4jcxT5FEue9D/6EH30llvCQxSSxrbysLCRC/KAUPSgSx8Xi4ZrWESka1IusJxn4iXMig9DLPmIZRJFPAII6Phrfwq1ZGxCSKdLB4WxqLCW8sWLXAHJJZ3l0hYNv/Mpf5jrxcMnGMwoQF4alk03WBx0wJmGo0yZcdMzZJUErg6EhXtEJ2VraFhv4huugVAIhtgH0JaXtoCiFDMMeglEpaweICQYR+hyPV+crBjI88in/PnScE5mXbWFqvuxzFxhWdnxUbYkg2cs+Yy+8goT7HUhB1wxSIXZMB370eRDzOho24tZ8pHrp0vwOQ9kTQppDB7ookWLItOgFFGzXhDFrbfeahoNsmAvhBFHQDSiEE8SwqoEsH7YcL0IsEqwd/fbu0OVH0tJYGZ9tmzNzq5BWmGImsKOA+VWioBQQhN8VyQaBrmeNB7oiiTkI4Obhpwr1ytFTjEf16n0LzIEJdPwAh0EhqDsAHOpXK+kVg8Im+WyoX3mfAsOaTysIGnEAwcOONcPBjPxaOByEdwFKD2kEgZcKNl2ZYKlBGSZ0dFEoc1/l7hGWL4kFlOpERfLEUhsaHYBQe1qRCz59PX0BjWO38IREmHtjwBC0FPzEA9khjUlKFXQ2V4CEAftcoWNBBpJ3vWSslxM0LggtQuBVQLa1ToZ4UZgxcjGNDHEExUnkvO4dUlkkHaRWa9KQK+TEoKxIYsmQaHT+XFwsVzypZFgcZwulAqx5PPJa9vNFPmkmNcVZNodUtGWUr94zx/Tazv0ZzwovxIf+9IuV5wprAlFzzzhZmkC0Z/hcDGvmcrXZelyJI5UachCPw19XMj0tA2mlIFZE9O8MpRIwiwLmXZHRlxAOw3lJLGmCgGWoDwDWcekYQL1mViXTlsKyDIMIH2UafEo5Esj5OMaMC4WsTEfyIIZrPo1q4OelEUTtY6n/tHVxvLoemRNzvnxyqrRxGRcrwdWZK9BUlFlu+KihG5X1BqdMOjRgNXQxH10gJljSEzKlGnMOJCPxqbhKQdLZ9SoUaYcKaPSAWdcJgiBTVYOi0ITAyrFylvKYHGhLNZj49yYMTU59dkxIayIlpaebAyKtTSynkfyofAtLQO7OhirjvU4spCwrm5tNhakLR7usVSQ2KIMnmzSl7Zt25addZUBDLfeTiNEI2t+6G+ui2OLRSz5QBbv/HCZWcPD1rV6Tc63nrFYIB6m0pmOtz9MdrGyfHSsx477YDUV+ynXJDEfLAshCpeHLQ0snYmGFHKQ1yCkLGlg19chWCcB4ch0PaB8pu2Z/hfQiZJO4ZcC8m6UzHKZd45SisT0dildCEiCuiAePSVt3jljdfKMhtDAclg+niXtQL5SWhpRkNXJ8m6WyIEMsiyh1HLIgKf7r/QlBjEZtGQBoE5DHqbFsa6ljCZr9fhAw/x7RdTnODUgmZm/2ZCNq5x4a7eJBcmqZRYg2tPx5GHtj2DP3T/KIadrnt+QM+tly/HdYx/lHMfJyUfiS71ocTCD52E/Iw+PoQLnd7twiXCpUPDT3YfNuU8zM1fEhcKm4u14D3nzAeIo5pOuBLrPJfLx8BjKSPRi6WW3f8/sP/rVb51cpKTvWUFW1fw9aQ8Pj7Nw/pgYQefaeTeb31+pnRCTOo2oVyoi0xe53qev91RR+T08PMoHZ8vn0gXzsr/PvyA+m+3+8Pc8BKttzP2gMxs3KvZViySzZS5xrqGAarkPj+pDXDzS+ZMaI6dNzR5HfQJDw7ZiovLgZvFJDp2v0FctvkhAWtUQqPUBZ4+hDCe3y47duMRlXMmnlK9aYDENtq8qenh4hMOJfDQhMMUeB1wn/fEve9WzRr/1PkXOVrlYZR4eHuUBBgHGSpj+O5GPXjksr0fkQ/9PaETnQTj9nlexr1qceDOeHKsVnc0PGVdMlkII9j/2hDnf0/mPsslCffbWvmhx2eoH1PfusntLXi73wrMuZXmVflalgN3/0O23brvTrP+z33wATjGfjhUrzWdQIYVPEk6F/7f3lPmERj4wba//1hjLieAxpKS/4ewC5Ktf80giGasFU5f/ODi8cVPw4fpfBA0tj5tzfT09waHU8eTFdwU1Dd8oqzwXN10XXDr/7ETFiMmTy1r/sJoasxUKnhvEXY642kA8q3LKD4Zlvlcke2KwfOEi6v/znMgHBov7mJgGr18cSRA0jkrv8sF4G2lLal9FXlStNEbUTQ6+liIgOt3lqx40ine4dZO5BjGVXZ6UAkF6lcLVG54tKv9/LAtyIDEQz6qc8oPhGaIXwscDYoOEwmK5VfmngUfaXqq0CBWDkAzWD1YPewgJYgK4Xpj0mMevf/2bxlQmnUBMZw37HL8hOMr5S+O3E8tIvbZbwfGu76jvFafqJB0bdWlwzHnkII/tZmo5tXskcuOKyf1jKYYBeeQaae375JnJc6Q8/QzBpzvfMLKJCxUlYxywXKhbZDi2dVvOdWlPeR7yXPPJ79IHuC7PmXtxabMw8EI6r1GFfRWjKskHN+9cnfVi1IFssHg+zCitEJLpdAvTHylvbNtoXDM6s5xLArvsJJi85C7TobOxgdSe40vnpy1dOv+nu94w8mHBoYCidOw5pl7ugTxCrC4gL6DsmhlXmrrCYmGXP/ygcYUA9Vy9Ifc7Q8iBG0s6I9PaJ7LXuB8IifzkBe2Lkj9jKYs6KKem4UpTrjw33Z7cT/p5zMsrv2sf6OnYa9qYthpRNym2zQpBVf5jKcRDYDzuG0TViqkZ8jmUif2IGSyKJwohoEMzSiYx+/Hr7XJsUKa2LFAIiLEuVQ+y0ek5FmKhg9OpyYPLVJvp2B9vfTXo3vg7cyydH8VC+UXBXEF6cccunn2dsQg+zhCJBsfpuMsboXVwjvsB/971N3MPEmdDaSFEuT5szGhjIaCsUfJGPSvtOkKW1CPPTdqTNGdjW9flld+1D2AJkUby5muzQlGVlg84+OTTlRahYqAjSqfRhMJoZnd+UfCTHclmwlDc2DSZkV82qQvF5NrHmQ7MnmPkxuIRoKxsKBGym/sxI/Fko8woTNIZPB3IFYupL+bby7HlpH5rt4V7wFoQ+ftOpq3wfLJGPSvycJ+QJK6PhrRnkqB6kj6g0+VrMwEDUhIrtCotH0DgmSD2uWr9FDPLUyqglFEjfd3iHxilYgRFwWSEFyKImiLnvma/vsVYdt2pkRp3oXFza9ln8vIB64zNjs/kQ9iz4rlAspBDw88eN4RWSIytVIhqMwFtg4yuqFryAftWP2pWZ0dN9Z1rwGxnJNYQBbmk6Xqzl2lSlEdGsdOHSz9rgkLRWYm5sJfRV+q/of2vkaOoxLWwgiQgOpjIx1gJKctQ3LBC0Z2ZqcwSsxXUlvbkvOtg49IHohDVZvq6fS4fjNtVrd/AIfZzYN25637ZmHrvT7KzNHRAfHw6EiOudJpLZqc7YOdPHzLXGenszuoKSEtiGWy2JQB5II+OG9RmAsjvLltu0lO3jLSA38QejPwZ5Rwo4vlqhvyQPYl7R3yEPMRXxPWKmlVzqV/u17YGpT2ljex6wuR36QP5ENZmAqy0uNkvDWP5TFl6d9V+R4cFjHwtsdT/jDoUIXGFD5/9uel8jF50Ij1jRRpGbGZv6JzG5E8dF7KiVxRCQy94k3UhdSouhUyNba1GcWUKGHJBRvYc426d7n4iG9RNGnR2Bc+GYDJy8Bxc1w1hlWHBdWeC/twTFkfSdTyUw1odngVl8AyIH53JWEBh7ck5qSdMfpc+kA9hbSYgsJ4E5jOq/Oi4vznRwsChBNyuG97ZVXXu11B+qx23LhvPKNI98SgPSt1m2dkuXm+YsvSeogscjMD94vvR1YahSjyY+nRiY7mserDS4ng4YCDaLGeqnXeiZr20qSpjQLztjnXnMTiAqc8s1WCYlfNwQ6nbLOt2eXh4eJQTVbvI0MPDY3DDk4+Hh0dF4MnHw8OjIvDk4+HhURH8HxsDGcb+WLUfAAAAAElFTkSuQmCCUEsHCP5DHJx0EgAAbxIAAFBLAwQUAAgICAAyVFJJAAAAAAAAAAAAAAAAFAAAAHhsL3NoYXJlZFN0cmluZ3MueG1sjVdLb9s4EL4v0P9AOJfdgyNR8iMOHAcLZwukCOJu7DbHgpZoi4lEaknKSfrrdyS7RaSh6Rg+SDPD4Ty+eWh6/VrkZMe1EUpe9eh52CNcJioVcnvV+7b63L/oXc8+/TE1xpJEVdKCUEiHPVJJ8V/F5wfSYNIjoEmaq15mbXkZBCbJeMHMuSq5BM5G6YJZeNXbwJSas9RknNsiD6IwHAUFE7I3mxoxm9pgNg3qh+ZldifgZrUhC52ClWSjVUGikI764bhPL0h//0JDeJkGtnX0hll+uecvEqvWXDuE5rng0pJcbYW8JIYV5y9MbrtSq7eSd2lftUqrxJJ7VmBesCD3VQFXHjv1b8WkFfbtGP+Bb7iGVCDVD7xU2hL+WvLE8pQo2ZX4zMBb/eY0bGmZrUyX+vct3Oe8bcmKMueGzFWew32Akj/v2JqsuLEAkb8+IL6QL0ynZJmJsoBI4yONRXWKTXNY84SLHU+vUWSWt12SBTO6NDqus92v0+6wjswBe3XYDkHqijwyUTtGAK/kO8tFymonUA7681xVaZ+OwigaUz879LJHEz8bAbbNRj622cMuW4PrudhmKGzMbBjCRRRFKLzh0fC+j92tNOUeAD4DaTxG+qMYyJjqkMMkb+YfHXXdMmaColXDnBwq0mEASvzKgcfWFRco2y43Yoe7g6O+tTIeIx9u+C4Xkh68oGdnZ3vKgYBvqkUcib/55/vd7f0P5HNdg8RUZQmNVBPAl4AKtsRfFBGKw8/XBFvi14Gww8DytdOfU6pQjQ40txp1w02GSmQ8iocjZEkzF6EFnwIEgK7LlvwFEBCD0fDkanClilFyUJXR+GN4ocj0NnvQZWcMhrDIeW2fap68CkIUeGbSjUldoHeUNMj61aPEQdCIK2o4ZCCouVc9neAq+Pb1YYFgccJMOkFm/oQfRuqHckZHOKaMeU8M0RSpQwQpbIoXOdk+i4z5UokESh0WvOQ5r9czYQhLU5gszXY2/01ndVNImglKmEyJsKSGD+Amhe0msb+OKZm/NUkDguZ7IhhnJM8YgXUTRa+p6VLJesPiGoUjPL4CtFwbeMFPBydOo3sBUvuybZxBbBoBF09skPcnwLtA0NhvJR4IKd/B338IQcIB1/YJb4OluMFmT9nTU5e4S7YJmtHFcw5/r3rcRVfcP4jxinHCQTxB1mu8MDnFvHpDZHqidD1BsZ4TilCNL6HmrCjwNwge7m1NJ9h47vq7T+iCICkPHzqRs13XEtCd/b0pnKDx1L4YsQ3KWPsAQkWbjZI7RK4NMaltMkrTAFk5wKS2DpSB9wGNvQH1ehjiFfi9ZurV7O0A4QRdbA/LNd5vTvSSdxUTGGNn/wNQSwcITHrawPgDAADFEAAAUEsDBBQACAgIADJUUkkAAAAAAAAAAAAAAAANAAAAeGwvc3R5bGVzLnhtbL1VXW/TMBR9R+I/WH5nacuGBkoyiUlFPK8gXt34JrHwR2S7I92v59pOm3RjI9sEL/X1ybnnfvjaza96JcktWCeMLujybEEJ6MpwoZuCftus313Sq/Ltm9z5vYSbFsAT9NCuoK333acsc1ULirkz04HGL7Wxinnc2iZznQXGXXBSMlstFh8yxYSmZa53aq28I5XZaV/QBc3KvDZ6RFY0AWXu7sgtk5hayA1plZHGEqE59MALehkwzRQk1jWTYmtF1GNKyH2CVwGImQ48JbSxAcxSlPQ76nwHy5lm0e1eAtu09XYHE4G4OBQSUh6rOKcJKPOOeQ9Wr3FDBnuz76Cg2uhBJvL+wubM/vxi2f4pD4Sa69MmfYz8E868eM5IwemzFOOCjdgay3GsDq24oAeozCXUHt2taNqwetOFthrvjUKDC9YYzWQIcPAY10AicRjxEIGLnaJ/IoYQD2mvdI8J/y9V7MoLQg0G9r8CKW8C70d9PIQlHkJfk3T7vvJw8UiY24OJJzeYSSZtgv5ULWlPZC9eJEv6+qj/mPdy9H7/iDdhXSf3YQSHOzkAJmQWgTLHR6HRCrQnrbHiDj+FG1whAOkR6OtZFczOYV5JJw05fyA3VpCAz5E7r6jwpHtRTZFflnUb6P3k6ZpZ9tN5/pu0smHG0Br/esrfUEsHCNOBNcL3AQAArwYAAFBLAwQUAAgICAAyVFJJAAAAAAAAAAAAAAAADwAAAHhsL3dvcmtib29rLnhtbI2QQUvEMBCF74L/YZiTl27SKutami6CCAVBD6v3bDPdhm2SksRdf75pl6pHT8Pjvfl4vGr7ZQY4kQ/aWYH5iiOQbZ3S9iDwffecbXBbX19VZ+ePe+eOkPI2COxjHEvGQtuTkWHlRrLJ6Zw3MibpDyyMnqQKPVE0Ays4XzMjtcULofT/Ybiu0y09ufbTkI0XiKdBxtQ29HoMWP80e/OgZKT8gd8J7OQQCFldTc6HpnP4DU4SZBv1iXZyL5BPOfYnOHdeLlhpSOBjA69epZ3gRYcINwXP1xm/z/INZFCk1XyplUDfqFuE+bFJMp/RC48tDepvUEsHCHCESpryAAAAdwEAAFBLAwQUAAgICAAyVFJJAAAAAAAAAAAAAAAAGgAAAHhsL19yZWxzL3dvcmtib29rLnhtbC5yZWxzrZHBTsMwDEDv/YrId5p2kxBCTXdBSLuy8QFR6jbV2iSyDdv+noAErBIIDjtZtuPnl6TZnOZJvSLxGIOBuqxAYXCxG8Ng4Hn/eHMHm7YomiecrOQz7MfEKg8FNuBF0r3W7DzOlsuYMOROH2m2klMadLLuYAfUq6q61XTJgLZQaoFV284Abbsa1P6c8D/42Pejw4foXmYM8sMWzd4SdjuhfCHOYEsDioFFucxU0L/6rK7qI+cJL0U+8j8M1tc0OEY6sEeUb4mv0vt75VB/+jR68e9t8QZQSwcI8M5YhtQAAAAwAgAAUEsDBBQACAgIADJUUkkAAAAAAAAAAAAAAAAYAAAAeGwvd29ya3NoZWV0cy9zaGVldDEueG1sjZxZbyJLGkTfR5r/gHifBmphadm+mqkiWUcazfqM7bKNrm0soNv35w/YbpKMCDK/t3b0ISuT43JlBcvVb3+8PLd+NtvdevN63e5967Zbzevd5n79+njd/s+/3V+G7d9u/vynq/fN9vfdU9PsW4cHvO6u20/7/dv3Tmd399S8rHbfNm/N6+F/Hjbbl9X+8OP2sbN72zar+48HvTx3sm6333lZrV/bnyN831rG2Dw8rO+aenP346V53X8Osm2eV/vDdHdP67dd++bqfn34v+P8W9vm4br91167c3P1cdj/rpv33dm/W8dV3G42vx9/mN1ftw+r3a9u/9U8N3f75vDzfvujOT66Qw93H5P6x7Z13zysfjzv/7l5nzbrx6f94Ukrj8/a7WrXVJvn/63v90/H7DSJerVf3VxtN++tw4p7h/neHf9xmGVrd/y58xX8DYMKgxqDMQYOgwkGUwxmGMwxWGCwPAs6h3WdFpedFpfh4jCoMKgxGGPgMJhgMMVghsEcgwUGy+zC4vLT4nJcHAYVBjUGYwwcBhMMphjMMJhjsMBgmV9YXHFaXIGLw6DCoMZgjIHDYILBFIMZBnMMFhgsiwuLK0+LKw+n+3V798XdXP286V11fh7wu1+rLXG1GNQYjDFwGEwwmGIww2COwQKDZXlhtf3TavuoEoMKgxqDMQYOgwkGUwxmGMwxWGCw7F9Y3OC0uMGZyuxDZQYqB7haDGoMxhg4DCYYTDGYYTDHYIHBcnBhtcPTaoe02hxWO8TVYlBjMMbAYTDBYIrBDIM5BgsMlsMLqx2dVjvCX1wMKgxqDMYYOAwmGEwxmGEwx2CBwXJ0YXG9rr/wd+nKj0lFSU3JmBJHyYSSKSUzSuaULChZnifhSs+2OJ8bhfzrl/f4a1vAr61AyhCpBNIPkVoggxAZC2QYIk4goxCZCKTXDZmpYuAyM1MM/P2aKwbO+oVi4CleKsY/x6E7v4Prfe6DivNH9VHeL8b/3mJSq3HITZpxihmiHcGM0A4zWRftMNNFOWKYHso5Z27VMMsoEqrx+89eblCTkxpMajUOqUkzTjGkRjCkhhlWwwypEcNkqCZPq4khoRq/e+4VBjUFqcGkVuOQmjTjFENqBENqmGE1zJAaMQz9SSvSamJIqMbv/XulQU1JajCp1TikJs04xZAawZAaZlgNM6RGDFOgmjKtJoaEavyNSq9vUNMnNZjUahxSk2acYkiNYEgNM6yGGVIjhilRTT+tJoaEavxtVm9gUDMgNZjUahxSk2acYkiNYEgNM6yGGVIjhumjmkFaTQwJ1fh7wt7QoIaZDJ7SSjHwlNaKGaG+NOMEk3dRn1jXAPWJcWiPzQzpE8PQVmGY1hdDQn3+Jrc3MuhjJs9Rn2AK1CeYEvWlGaeYPuoTDOkTDOljhvSJYYaob5TWF0PCgtvfxWfdtD7B5HBGVIIpYIK1YuDpGhsYp+aD+hSD+hSD+gSD+tSU8ewLGK0vioT6fDWR9Qz6mKGzTzBFjvoEU6C+NOMUU6I+MWfSxwxd+wRD+sR08NoXMBf0xZBQ39nrS4Z2QjAFXvsUg9c+xeC1z8A4xZA+ZkrslwTD+tINhjoUNhhZusGIIqE+32BkhgZDMGWG+gRDZx/2HmNKnBiH1YhjkRpDgyEYUiMOhfdiWbrBiCKhGt9gZIYGQzAltraKwdr2xHg1mDgxDqsRxyI1hgZDMKRGHGqAatINRhQJ1fgGIzM0GIIph6hGMCNUw0y/i3/00owTDOsT4/RQn6HlEAzpE4eiLUe65YgioT7fcmSGlkMwfdpyCAY3/IrBDb+BcYqhHaOhCREM60s3IWo6dPalm5AoEurzTUhmaEIEwxt+wZSojxne8KcZJxjWJ+ZDO0ZDWyIY0iemg/drWbotiSKhPt+WZIa2RDB90scMX9d+Mf66hokT4/C9mDgWXddE60Jq0k2IQAZdVJNuQqJIqMY3IZmhCRHMoIdqBJOhGsHkeGalGScY1mdoQgTD+tJNiJoy7RjTTUgUCd8N55uQ3NCECGaAO0bF4JmlGKyRDYwTDOlTDOoTDOkTDOpTU8Y/jHm6CYkioT7fhOR8Gz8YoT5mhl3UJ5oHvK6pY+HZZ2CcOhbpEw0PbksEw/rSTYh6evBWOk83IVEk1OebkNzQhAhmiLfSgqFb6Rzf3TGmxKlj5ajG0HIIhtWkWw41HfzDmKdbjigSqvEtR25oOQQzpD+MhpYjp5aDEqeORWoMLYdgWE265VDTwf4wT7ccUSRU41uO3NByCGaI/aFgaDd4YrwaajnUsUiNoeUQDKtJtxxqOnQ9SrccUSRU41uO3NByCGaIG3XBjPCFFcHwdiLNOHWsHupjhrcThpZDMKRPTAdbjjzdckSRUJ9vOXJDy5HTezkoqcU4rCbNOMGwGjz6VDyKRaT7CnVwfL9Tnu4rokgowvcVuaGvyOmdG5TUYhwWkWacYFgEHn0qHsUi0s2DOjhtA9LNQxQJRfjmITc0D4IZ0TZAMHStEQzJSjNOMPQ+DcXQHzTD+zQEQ/rElOl6lG4nokioz7cTuaGdODH+PMKkFuPweZRmnGD4PMKjT8Wj+DxK9wzq4CMUke4Zokj4wTTfMxSGnuHE+E+rYVJTMqbEiWPRk0yPmopH0ZMsGHyS1UK7WMYV6TogioTPsq8DCsMbIxTTxTZOQHTXYhlobIGcgFiYmBFuoAXDCtONgJwzbsGKdCUQRUKFvhIoDJWAYrr4SpOA8CX4uqBOgBInxmE5hk5AMCwn3QnIteNuoEiXAlEklONLgcJQCgiG3nikGKzbBEMvIxkYJxj2J+aDuwHBsL90caCewi6+IbpINwdRJPR39vloQ3OgmG4fBSpogAYVNESFBsgJiB2KgbAyFQw7TDcMcs64kwigCw7NFUPhK4bCUDEIhl7KVQy+Eq+OhR8ZHFsgJyA2aCgZBMMG0yWDnDO23kW6ZYgioUHfMhSGlkEx+CHLSkC8S0kzYwPjBMMCxTh0GTSUE4Ihger5wXaiSLcTUSQU6NuJwtBOKAY/3VpJiC6ECuqjQgPkBMQOmWGHhl5DMORQzRnfEVOki40oEjr0xUZhKDYUgx/YqQTEJ6EaCN+Ga4GcgFih4Z0XgmGF6W5DzRnHWRTpciOKhAp9uVEYyg3F4GedKwnRvYKA+DQ0QE5A7JAZdmioRQRDDtXq6U9puheJIuF32vhepDT0IoKhlxkFQ2dhSd0JJU6MQ27UsdCNYMiNYNCNenrok61luk2JIqEb36aUhjZFMfj5zkpALMcw0NgCOQGxQkObIhhWmG5T5JzxpeIy3aZEkVChb1NKQ5uiGPqcpYBYoRoI7/cskBMQKzR0LoJhhenORc4Z7/fKdOcSRUKFvnMpDZ2LYvgsNLwTwzLQ2AI5AbFCw/s1BMMK07WLmjO+KLQo07VLFAkV+tqlFH0BKRQMvtpUCYgVioFYoQFyAmKFYkakkBlWyAwpVM8Q1tIBdEFhDAkVnn35nqF1UQyfheLzK6TQ8C0dBsYJhgWK+ZBAQ+kiGBIopozV8KJMly5RJBToS5fSULooBjuxygLVEqKT0AA5AbFDQ+8iGHaY7l3knGk3k+5dokjo0PcupaF3UQy2iJUFqiVE2xkD5ATEDg29i2DYYbp3kXOm7Uy6d4kioUPfu5SG3kUx+L0ClYD4D6kaCF9EskBOQKzQ0LsIhhWmexc5Z7oWpnuXKBIq9L1LaehdFIPfLVAJiBWqgfALCCyQExArNNQugmGF6dpFzhlfCCzTtUsUCb9c19cufUPtohj8joRKQviXtE/FCyVODER21MHwBSLB0Ju2BIN25LrwXVv9dPESRT7tdM6+aP2l2T42VfP8vDv799c3w5ffZx/fy945h95Wj83fV9vH9euudbvZ7zcv1+3ut8Fhh/Sw2eyb7fGnwy3LU7O6P/3w3DzsP6h2a/v5JfAf/95v3r4eezjI/Xb1vn59bG2/r++v29vZ/ed3pZ6+SP/m/1BLBwgXHwSgpwsAAHxfAABQSwMEFAAICAgAMlRSSQAAAAAAAAAAAAAAACMAAAB4bC93b3Jrc2hlZXRzL19yZWxzL3NoZWV0MS54bWwucmVsc42PuwrCMBSG9zxFOLtJ6yAiTbuI0FXqA4TkNC02F5J4e3uzKAoOjue/fIe/6e52oVeMafZOQM0qoOiU17MzAk7DYbWFriWkOeIic8mkaQ6JlpJLAqacw47zpCa0MjEf0BVn9NHKXM5oeJDqLA3ydVVtePxkQEso/cLSXguIva6BDo+A/+D9OM4K915dLLr84wvXUd7KloKU0WAWwNhLe5s1K1jgLWn418yWPAFQSwcI+uZBOawAAAAfAQAAUEsBAhQAFAAICAgAMlRSSW4aD5IEAQAA9AEAABEAAAAAAAAAAAAAAAAAAAAAAGRvY1Byb3BzL2NvcmUueG1sUEsBAhQAFAAICAgAMlRSSVcoXiPjAAAARgIAAAsAAAAAAAAAAAAAAAAAQwEAAF9yZWxzLy5yZWxzUEsBAhQAFAAICAgAMlRSSfE87KRTAQAA2AQAABMAAAAAAAAAAAAAAAAAXwIAAFtDb250ZW50X1R5cGVzXS54bWxQSwECFAAUAAgICAAyVFJJNm6DIZMAAAC4AAAAEAAAAAAAAAAAAAAAAADzAwAAZG9jUHJvcHMvYXBwLnhtbFBLAQIUABQACAgIADJUUklc1mxhugEAANEDAAAYAAAAAAAAAAAAAAAAAMQEAAB4bC9kcmF3aW5ncy9kcmF3aW5nMS54bWxQSwECFAAUAAgICAAyVFJJKbETAK0AAAAYAQAAIwAAAAAAAAAAAAAAAADEBgAAeGwvZHJhd2luZ3MvX3JlbHMvZHJhd2luZzEueG1sLnJlbHNQSwECFAAUAAgICAAyVFJJ/kMcnHQSAABvEgAAEwAAAAAAAAAAAAAAAADCBwAAeGwvbWVkaWEvaW1hZ2UxLnBuZ1BLAQIUABQACAgIADJUUklMetrA+AMAAMUQAAAUAAAAAAAAAAAAAAAAAHcaAAB4bC9zaGFyZWRTdHJpbmdzLnhtbFBLAQIUABQACAgIADJUUknTgTXC9wEAAK8GAAANAAAAAAAAAAAAAAAAALEeAAB4bC9zdHlsZXMueG1sUEsBAhQAFAAICAgAMlRSSXCESpryAAAAdwEAAA8AAAAAAAAAAAAAAAAA4yAAAHhsL3dvcmtib29rLnhtbFBLAQIUABQACAgIADJUUknwzliG1AAAADACAAAaAAAAAAAAAAAAAAAAABIiAAB4bC9fcmVscy93b3JrYm9vay54bWwucmVsc1BLAQIUABQACAgIADJUUkkXHwSgpwsAAHxfAAAYAAAAAAAAAAAAAAAAAC4jAAB4bC93b3Jrc2hlZXRzL3NoZWV0MS54bWxQSwECFAAUAAgICAAyVFJJ+uZBOawAAAAfAQAAIwAAAAAAAAAAAAAAAAAbLwAAeGwvd29ya3NoZWV0cy9fcmVscy9zaGVldDEueG1sLnJlbHNQSwUGAAAAAA0ADQBoAwAAGDAAAAAA";
		byte[] code = Base64.decode(str);
		OutputStream os = new FileOutputStream("E:\\roy.xlsx");
		os.write(code);
		os.flush();
		os.close();
//		System.out.println(result);
		// return new BASE64Encoder().encode(buffer);

	}

}
