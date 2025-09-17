import jsx from "../jsx/pragma";

interface SVGIconProps {
    icon: string;
    className?: string;
}

export default function Svg({icon, className = ""}: SVGIconProps) {
    return <div className={className} innerHTML={icon}></div>
}