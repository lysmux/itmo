import jsx from "./jsx/pragma"
import {ArrayObserver, Observer} from "./jsx/observer";

import Toast from "./components/Toast";

const errors = new ArrayObserver(["err1", "err2", "err3"]);

const App = () => {
    return (
        <div>
            <Toast title="Error" message="abc"/>
            <Toast title="Error" message="abc"/>
            <Toast title="Error" message="abc"/>
        </div>
    )
}

export default App