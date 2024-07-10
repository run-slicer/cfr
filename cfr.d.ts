declare module "cfr" {
    export type Options = Record<string, string>;

    export interface Config {
        source?: (name: string) => Promise<string | null>;
        options?: Options;
    }

    export function decompile(name: string, config?: Config): Promise<string>;
}
