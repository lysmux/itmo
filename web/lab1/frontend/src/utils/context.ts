const GLOBAL_CONTEXT: Record<string, any> = {}

export function setVar(key: string, value: any) {
    if (GLOBAL_CONTEXT[key]) {
        throw new Error(`${key} is already set`);
    }
    GLOBAL_CONTEXT[key] = value;
}

export function getVar<T>(key: string, defaultValue?: T): T {
    return GLOBAL_CONTEXT[key] || defaultValue
}