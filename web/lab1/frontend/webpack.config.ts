import path from "path";
import HtmlWebpackPlugin from "html-webpack-plugin";
import webpack from "webpack";
import type {Configuration as DevServerConfiguration} from "webpack-dev-server";
import MiniCssExtractPlugin from "mini-css-extract-plugin";

interface EnvVars {
    mode: webpack.Configuration["mode"];
    port: number;
}

export default (env: EnvVars) => {
    const isDev = env.mode === "development";

    const config: webpack.Configuration = {
        mode: env.mode ?? "production",
        devtool: 'inline-source-map',
        entry: path.resolve(__dirname, "src/index.ts"),
        output: {
            path: path.resolve(__dirname, "dist"),
            filename: "[name].[contenthash:8].js",
            assetModuleFilename: "assets/[name][ext]",
            clean: true
        },
        plugins: [
            new HtmlWebpackPlugin({
                template: path.resolve(__dirname, "public/index.html"),
                favicon: path.resolve(__dirname, "src/assets/favicon.ico")
            }),
            new MiniCssExtractPlugin({filename: "[name].[contenthash:8].css"})
        ],
        module: {
            rules: [
                {
                    test: /\.s[ac]ss$/i,
                    use: [
                        MiniCssExtractPlugin.loader,
                        {
                            loader: "css-loader",
                            options: {
                                modules: {
                                    localIdentName: isDev ? "[path][name]__[local]" : "[hash:base64:5]",
                                    namedExport: false,
                                    exportGlobals: true
                                },
                            }
                        },
                        "sass-loader"
                    ],
                },
                {
                    test: /\.tsx?$/,
                    use: "ts-loader",
                    exclude: /node_modules/,
                },
                {
                    oneOf: [
                        {
                            test: /\.svg$/i,
                            resourceQuery: /source/,
                            type: "asset/source",
                        },
                        {
                            test: /\.(png|svg|jpg|jpeg|gif)$/i,
                            type: "asset/resource",
                        }
                    ]
                },
                {
                    test: /\.(woff|woff2|eot|ttf|otf)$/i,
                    type: "asset/resource",
                },
            ],
        },
        resolve: {
            extensions: ['.tsx', '.ts', '.js'],
        },
        devServer: {
            port: env.port ?? 5000,
            open: true
        }
    }

    return config;
}