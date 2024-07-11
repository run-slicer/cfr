declare module "@run-slicer/cfr" {
    export type Options = Record<string, string>;

    export interface Config {
        source?: (name: string) => Promise<Uint8Array | null>;
        options?: Options;
    }

    export function decompile(name: string, config?: Config): Promise<string>;
}
