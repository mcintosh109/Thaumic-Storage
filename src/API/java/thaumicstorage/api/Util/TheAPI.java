package thaumicstorage.api;

import java.lang.reflect.Method;

/**
 * API entry point
 *
 * @author BrockWS
 * @version 1.0.0
 * @since 1.0.0
 */
public class  TheAPI {

    private static itheapi API;

    /**
     * Gets the instance of the Thaumic Storage API, will cache it if it isn't cached hopfuly
     *
     * @return API Instance
     */
    public static itheapi instance() {
        if (ThEApi.API == null) {
            try {
                Class clazz = Class.forName("thaumicstorage.ThaumicStorageApi");
                Method instanceAccessor = clazz.getMethod("instance");
                TheAPI.API = (itheapi) instanceAccessor.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TheAPI.API;
    }

}
