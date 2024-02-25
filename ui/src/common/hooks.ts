import { useCookies } from "react-cookie";
import { COOKIE_KEY_ADMIN_AUTH, COOKIE_KEY_USERNAME } from "./constants";
import React from "react";

export const useUsername = (): string | null => {
    const [cookies, ,] = useCookies([COOKIE_KEY_USERNAME])
    const username = cookies[COOKIE_KEY_USERNAME]
    return username || null
}

export const useIsAdmin = (): boolean => {
    const [cookies, ,] = useCookies([COOKIE_KEY_ADMIN_AUTH])
    return cookies[COOKIE_KEY_ADMIN_AUTH]
}

export function useCallbackRef<T>(): [T | null, (node: T) => void] {
    const [ref, setRef] = React.useState<T | null>(null);

    const attachRef = React.useCallback((node: T) => {
        setRef(node);
    }, []);

    return [ref, attachRef]
}
