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
            clean: true
        },
        plugins: [
            new HtmlWebpackPlugin({template: path.resolve(__dirname, "public/index.html")}),
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
                                    namedExport: false
                                },
                            }
                        },
                        "sass-loader"
                    ],
                },
                {
                    test: /\.tsx?$/,
                    use: 'ts-loader',
                    exclude: /node_modules/,
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